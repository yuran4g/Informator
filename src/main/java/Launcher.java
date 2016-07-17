/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Launcher{
    public static void main(String[] args) {
        if (args.length==0)
            new Informator();
        else
            new Informator(args);
    }
}