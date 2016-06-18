package im.abe.panda;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        Template template = Template.from("Hello, [person.name]! There's a message for you: [message]");
        Map<String, Object> context = new HashMap<>();
        context.put("person", new Object() {
            private String name = "Jean";
        });
        context.put("message", "Hi.");

        System.out.println(template.renderToString(context));
    }
}
