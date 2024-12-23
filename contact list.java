//Node class
class Node{
    String name;
    String phoneNumber;
    SinglyLinkedList contactslList;
    Node next;
    Node(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.contactslList = null;
        this.next = null;
    }
}

//Singly Linked List class
class SinglyLinkedList{
    Node head;
    SinglyLinkedList(){
        head = null;
    }

    public void addToHead(String name, String phoneNumber){
        Node newNode = new Node(name, phoneNumber);
        newNode.next = head;
        head = newNode;
    }

    public void addToTail(String name, String phoneNumber){
        Node newNode = new Node(name, phoneNumber);
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        newNode.next = null;
        temp = newNode;
    }

    public String search(String name){
        Node temp = head;
        while (temp != null) {
            if (temp.name.equals(name)) {
                return temp.phoneNumber;
            }
            temp = temp.next;
        }
        return null;
    }

    public void delete(String name){
        if (head == null) {
            System.out.println("There is no node to be deleted");
            return;
        }
        if (head.name.equals(name)) {
            head = head.next;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            if (temp.next.name.equals(name)) {
                temp.next = temp.next.next;
                break;
            }
            temp = temp.next;
        }
        System.out.println("There is no node with this name already");
    }

    public void displayAll(){
        Node temp = head;
        while (temp != null) {
            System.out.println("  -> Name: " + temp.name + ", Phone: " + temp.phoneNumber);
            temp = temp.next;
        }
    }
}

//Hash Table class
class HashTable{
    Node [] table; //Array of Node objects
    int tableSize;
    HashTable(int size){
        this.tableSize = size;
        table = new Node [size]; //create an array of Node objects
    }

    public static long calc_hash(String key, int tableSize){
        int l = key.length();
        long hash = 0;
        for(int i = 0; i < l; i++){
            hash += Character.getNumericValue(key.charAt(i)); //step2:Get numeric value
            hash += (hash << 10); //step3:Amplify influence of char.
            hash ^= (hash >> 6); //step4:Mix the bits
        }
        //step5:repeat(3,4) to randomize the value of hash
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);

        //step6:keep the hash within range
        if (hash < 0) {
            hash = -hash;
        }
        return hash % tableSize;
    }

    //Insert a new contact into the hash table
    public void insert(String name, String PhoneNumber){
        int index = (int) calc_hash(name, tableSize);

        //bucket is empty
        if (table[index] == null) {
            table[index] = new Node(name, PhoneNumber);
            table[index].contactslList = new SinglyLinkedList();
        }
        //Handle collision by adding to the linked list
        else{
            table[index].contactslList.addToTail(name, PhoneNumber);
        }
    }

    //Search for a contact by name
    public String search(String name){
        int index = (int) calc_hash(name, tableSize);
        Node node = table[index]; //Get the bucket's node

        if (node != null) {
            //Search in the main node first
            if (node.name.equals(name)) {
                return node.phoneNumber;
            }
            //If not found, Search in the linked list
            return node.contactslList.search(name);
        }
        return null; //Return null if not found
    }

    //Delete contact by name
    public void delete(String name){
        int index = (int) calc_hash(name, tableSize);
        Node node = table[index];

        if (node != null) {
            //Check if the main node matches
            if (node.name.equals(name)) {
                //Make sure if it has linked list
                if (node.contactslList.head != null) {
                    //Promote the first linked list node to main node
                    Node newNode = node.contactslList.head;
                    table[index] = new Node(newNode.name, newNode.phoneNumber);
                    table[index].contactslList = node.contactslList;
                    table[index].contactslList.head = newNode.next; // Update linked list
                }
                //There is no linked list
                else{
                    table[index] = null; //set bucket to null
                }
            }
            //If not, delete from the linked list
            else{
                node.contactslList.delete(name);
            }
        }
        else{
        System.out.println("There is no node to be deleted");
        }
    }

    //Update a contact number
    public void update(String name, String newPhoneNumber){
        int index = (int) calc_hash(name, tableSize);
        Node node = table[index];

        if (node != null) {
            //Check if the main node matches
            if (node.name.equals(name)) {
                node.phoneNumber = newPhoneNumber;
            }
            //If not, update the linked list
            else{
                if (node.contactslList.search(name) != null) {
                    //Traverse the list and update the phone number
                    Node temp = node.contactslList.head;
                    while (temp != null) {
                        if (temp.name.equals(name)) {
                            temp.phoneNumber = newPhoneNumber;
                            break;
                        }
                        temp = temp.next;
                    }
                }
                else{
                    System.out.println("There is no node to be updated");
                }
            }
        }
        else{
            System.out.println("There is no node to be updated");
        }
    }

    //Display all contacts currently stored in the phone list
    public void displayAll(){
        for(int i = 0; i < tableSize; i++){
            Node node = table[i];
            if (node != null) {
                //Print main node
                System.out.println("Bucket " + i + ":");
                System.out.println("Name: " + node.name + ", Phone: " + node.phoneNumber);

                //Print linked list contents
                if (node.contactslList != null) {
                    node.contactslList.displayAll();
                }
            }
        }
    }
}


public class Assignment2 {
    public static void main(String[] args) {
        HashTable phoneBook = new HashTable(10);

        // Insert some contacts
        phoneBook.insert("John", "123-456-7890");
        phoneBook.insert("Alice", "234-567-8901");
        phoneBook.insert("Bob", "345-678-9012");

        // Display all contacts
        System.out.println("Phone Book:");
        phoneBook.displayAll();

        // Search for a contact
        System.out.println("\nSearch for Alice: "+ phoneBook.search("Alice"));

        // Update a contact number
        phoneBook.update("Alice", "999-999-9999");
        System.out.println("\nUpdated Alice's phone number:");
        phoneBook.displayAll();

        // Delete a contact
        phoneBook.delete("Bob");
        System.out.println("\nAfter deleteing Bob:");
        phoneBook.displayAll();
    }
}