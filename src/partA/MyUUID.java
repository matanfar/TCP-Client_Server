package partA;
import java.util.UUID;

public final class MyUUID implements Comparable<MyUUID>{
    final private String key;
    final private UUID uuid;
    //add final to this data members

    public static UUID Encoder(final String key){
        byte[] arr = key.getBytes();
        UUID newUuid = UUID.nameUUIDFromBytes(arr);
        return newUuid;
    }

    public UUID getUuid(){
        return this.uuid;
    }

    @Override
    public String toString() {
        return "key = " + key;
    }

    public MyUUID(String key){
        this.key = key;
        this.uuid = Encoder(key);
    }

    public String getStringUUID(){
        return this.key;
    }

    public static String Decoder(final MyUUID myUUID){
        return myUUID.getStringUUID();
    }

    @Override
    public int compareTo(MyUUID comperedMyuuid) {
        return this.uuid.compareTo(comperedMyuuid.getUuid());
    }


    public static void main(String[] args) {
        String str = "123", str2 = "5678";
        MyUUID my = new MyUUID(str);
        MyUUID my2 = new MyUUID(str2);
        System.out.println(my.Encoder(str));
        System.out.println(my.Decoder(my));
        System.out.println(my.compareTo(my2));
        System.out.println(my);


    }

}


