/**
 * Created by Андрей on 16.07.2016.
 */
public class Reg {
    public String path, key, version, value, pathSP, keySP;

    public Reg(){}

    public Reg(String Path, String Key, String Value, String Version, String PathSP, String KeySP) {
        path = Path;
        key = Key;
        version = Version;
        value = Value;
        pathSP = PathSP;
        keySP = KeySP;
    }

    String getPath() {
        return path;
    }

    String getKey() {
        return key;
    }

    String getVersion() {
        return version;
    }

    String getValue() {
        return value;
    }

    String getPathSP() {
        return pathSP;
    }

    String getKeySP() {
        return keySP;
    }
}
