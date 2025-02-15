package allfish.modid;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.*;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;


public class RegisterKeyBindings {
    private static final String CATEGORY = "AllFish";
    public static KeyBinding toggleKey;

    public static void register() {
        registerToggleKey();
    }

    private static void registerToggleKey() {
        toggleKey = new KeyBinding
                ("Toggle Fishing", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_L, CATEGORY);
        KeyBindingHelper.registerKeyBinding(toggleKey);
    }
}
