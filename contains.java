public class contains {
    public static void main(String[] args) {
        String[] arrey1 = new String[2];
        arrey1[0] = "A:0";
        arrey1[1] = "a:0";
        System.out.println(str_contains("a:",arrey1));
    }
    public static boolean str_contains(String str, String container) {
        char[] container_char = container.toCharArray();
        boolean is;
        for (int i = 0; i < container.length()-str.length()+1; i++) {
            if (container_char[i] == str.toCharArray()[0]) {
                is = true;
                for (int j = 0; j < str.length(); j++) {
                    if (! (container_char[i+j] == str.toCharArray()[j])) {
                        is = false;
                        break;
                    }
                }
                if (is) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean str_contains(String str, String[] container) {
        for (int i = 0; i < container.length; i++) {
            if (str_contains(str, container[i])) {
                return true;
            }
        }
        return false;
    }
}
