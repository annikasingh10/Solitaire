public class SolitaireCipher {
 public Deck key;
 
 public SolitaireCipher (Deck key) {
  this.key = new Deck(key); // deep copy of the deck
 }
 
 /* 
  * TODO: Generates a keystream of the given size
  */
 public int[] getKeystream(int size) {
  /**** ADD CODE HERE ****/
  int[] keystream = new int[size];
  for(int i = 0; i < size; i++) {
   keystream[i] = key.generateNextKeystreamValue();
  }
  return keystream;
  //return null;
 }
  
 /* 
  * TODO: Encodes the input message using the algorithm described in the pdf.
  */
 public String encode(String msg) {
  /**** ADD CODE HERE ****/
  String tempMsg = "";
  for(int i = 0; i < msg.length(); i++) {
   char character = msg.charAt(i);
   if(!(character >= 'A' && character <= 'Z') && !(character >= 'a' && character <= 'z')) {
    continue;
   }
   character = Character.toUpperCase(character);
   tempMsg += character;
  }
  int[] keystream = getKeystream(tempMsg.length());
  String encodedMsg = "";
  for(int i = 0; i < tempMsg.length(); i++) {
   char character = (char) (((tempMsg.charAt(i) - 'A') + keystream[i]) % 26 + 'A');
   encodedMsg += character;
  }
  return encodedMsg;
  //return null;
 }
 
 /* 
  * TODO: Decodes the input message using the algorithm described in the pdf.
  */
 public String decode(String msg) {
  /**** ADD CODE HERE ****/
  String decodedMsg = "";
  int[] keystream = getKeystream(msg.length());
  for(int i = 0; i < msg.length(); i++) {
   char character = (char) (((msg.charAt(i) - 'A') - keystream[i] + (26 * 3)) % 26 + 'A');
   decodedMsg += character;
  }
  return decodedMsg;
  //return null;
 }
 
}
