package im.abe.panda;

import im.abe.panda.internal.ast.Node;
import im.abe.panda.internal.ast.Text;
import im.abe.panda.internal.ast.Value;
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

public class Interpreter {
    public void execute(@NotNull Template template,
                        @NotNull Node node,
                        @NotNull Map<String, Object> context,
                        @NotNull BufferedWriter writer) throws IOException {

        if (node instanceof Text) {
            Text text = (Text) node;
            writer.write(text.getValue());
        } else if (node instanceof Value) {
            Value value = (Value) node;
            Object evaluated = evaluate(value, context);
            if (value.isRaw()) {
                writer.write(String.valueOf(evaluated));
            } else {
                writer.write(template.escape(String.valueOf(evaluated)));
            }
        }

    }

    @Nullable
    private Object evaluate(Value value, Map<String, Object> context) {
        if (value.getLeft() != null) {
            Object left = evaluate(value.getLeft(), context);
            if (left == null) {
                return null;
            } else if (left instanceof Map<?, ?>) {
                return ((Map<?, ?>) left).get(value.getName());
            } else {
                return tryGetter(value.getName(), left);
            }
        } else {
            return context.get(value.getName());
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
