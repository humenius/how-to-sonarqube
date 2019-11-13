public class SnatChat {

    public static void main(String[] args) {
        SnatChatRoom room = new SnatChatRoom("GansGeheim");

        room.register(new SnatChatWindow(room, new Account("Bob")));
        room.register(new SnatChatWindow(room, new Account("Alice")));

        String test = Message.rot13("amAMnzNZ");
        System.out.println(test);
        System.out.println(Message.rot13(test));

        System.exit(0);
    }


}
