import java.lang.reflect.Method;
public class FindMethod {
    public static void main(String[] args) throws Exception {
        System.out.println("Methods in Player:");
        for(Method m : net.minecraft.world.entity.player.Player.class.getDeclaredMethods()) {
            if(m.getName().toLowerCase().contains("respawn")) {
                System.out.println(m);
            }
        }
    }
}
