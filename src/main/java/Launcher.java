/**
 * Created by yksenofontov on 08.07.2016.
 */
public class Launcher{
    public static void main(String[] args) {
        Informator informator;
        if (args.length==0)
            informator = new Informator();
        else
            informator=new Informator(args);
    }
}