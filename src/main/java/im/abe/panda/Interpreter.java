package im.abe.panda;

import im.abe.panda.internal.ast.Node;
import im.abe.panda.internal.ast.Text;
import im.abe.panda.internal.ast.Variable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * The main runtime interpreter for template instances.
 */
public class Interpreter {
    /**
     * Render the given node to an output.
     *
     * @param template The template to render within.
     * @param node     The node.
     * @param context  The variable context to access.
     * @param writer   The output to write to.
     * @throws IOException If an IO error occurs while writing.
     */
    public void execute(@NotNull Template template,
                        @NotNull Node node,
                        @NotNull Map<String, Object> context,
                        @NotNull BufferedWriter writer) throws IOException {

        if (node instanceof Text) {
            Text text = (Text) node;
            writer.write(text.getValue());
        } else if (node instanceof Variable) {
            Variable variable = (Variable) node;
            Object evaluated = evaluate(variable, context);
            if (variable.isRaw()) {
                writer.write(evaluated == null ? "" : evaluated.toString());
            } else {
                writer.write(template.getEscapeStrategy().on(String.valueOf(evaluated == null ? "" : evaluated.toString())));
            }
        }

    }

    /**
     * Evaluate the given variable in the context. Tries both getters and direct field access.
     *
     * @param variable The variable to access.
     * @param context  The context to find it in.
     * @return The variable's value, or null if not found.
     */
    @Nullable
    private Object evaluate(Variable variable, Map<String, Object> context) {
        if (variable.getLeft() != null) {
            Object left = evaluate(variable.getLeft(), context);
            if (left == null) {
                return null;
            } else if (left instanceof Map<?, ?>) {
                return ((Map<?, ?>) left).get(variable.getName());
            } else {
                return tryGetter(variable.getName(), left);
            }
        } else {
            return context.get(variable.getName());
        }
    }

    private Object tryGetter(@NotNull String name, @NotNull Object inObject) {
        Class<?> clazz = inObject.getClass();
        try {
            Method getter = new PropertyDescriptor(name, clazz).getReadMethod();
            if (getter == null) {
                return getByField(name, inObject, clazz);
            } else {
                getter.setAccessible(true);
                return getter.invoke(inObject);
            }
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            return getByField(name, inObject, clazz);
        }
    }

    private Object getByField(@NotNull String name, @NotNull Object inObject, @NotNull Class<?> clazz) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(inObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
