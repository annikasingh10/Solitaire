import java.util.Random;

public class Deck {
 public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
 public static Random gen = new Random();

 public int numOfCards; // contains the total number of cards in the deck
 public Card head; // contains a pointer to the card on the top of the deck

 /* 
  * TODO: Initializes a Deck object using the inputs provided
  */
 public Deck(int numOfCardsPerSuit, int numOfSuits) {
  /**** ADD CODE HERE ****/
  //Illegal Arguments
  if((numOfSuits < 1 || numOfSuits > suitsInOrder.length) || (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13)) {
   throw new IllegalArgumentException("The inputs are illegal as they are either not a possible number of cards per suits or a number of suits.");
  }
  for(int i = 0; i < numOfSuits; i++) { //For every suit, starting from clubs
   for(int j = 0; j < numOfCardsPerSuit; j++) { //Making every card, starting from 1 (Ace)
    Card newCard = new PlayingCard(suitsInOrder[i], j + 1);
    addCard(newCard);
   }
  }
  //Adding both jokers
  addCard(new Joker("red"));
  addCard(new Joker("black"));
 }

 /* 
  * TODO: Implements a copy constructor for Deck using Card.getCopy().
  * This method runs in O(n), where n is the number of cards in d.
  */
 public Deck(Deck d) {
  /**** ADD CODE HERE ****/
  Card copyCard = d.head;
  for(int i = 0; i < d.numOfCards; i++) {
   addCard(copyCard.getCopy());
   copyCard = copyCard.next;
  }
}

 /*
  * For testing purposes we need a default constructor.
  */
 public Deck() {}

 /* 
  * TODO: Adds the specified card at the bottom of the deck. This 
  * method runs in $O(1)$. 
  */
 public void addCard(Card c) {
  /**** ADD CODE HERE ****/
  if(head == null) {
   head = c;
   head.prev = c;
   head.next = c;
   this.numOfCards = 1;
  }
  else {
   Card lastCard = head.prev;
   c.next = head;
   head.prev = c;
   lastCard.next = c;
   c.prev = lastCard;
   numOfCards += 1;
  }
 }

 /*
  * TODO: Shuffles the deck using the algorithm described in the pdf. 
  * This method runs in O(n) and uses O(n) space, where n is the total 
  * number of cards in the deck.
  */
 public void shuffle() {
  /**** ADD CODE HERE ****/
  if (numOfCards < 1) {
   return;
  }
  Card[] arrCopies = new Card[numOfCards];
  Card cur = head;

  //copy all cards into array
  for (int i=0; i<numOfCards; i++) {
   arrCopies[i] = cur;
   cur = cur.next;
  }

  //shuffle
  for (int i=numOfCards-1; i>0; i--) {
   int j = gen.nextInt(i+1);
   Card temp = arrCopies[i];
   arrCopies[i] = arrCopies[j];
   arrCopies[j] = temp;
  }

  //rebuild deck
  head = arrCopies[0];
  cur = head;
  for (int i=1; i<numOfCards; i++) {
   cur.next = arrCopies[i];
   cur.next.prev = cur;
   cur = cur.next;
  }
  head.prev = cur;
  cur.next = head;

 }

 /*
  * TODO: Returns a reference to the joker with the specified color in 
  * the deck. This method runs in O(n), where n is the total number of 
  * cards in the deck. 
  */
 public Joker locateJoker(String color) {
  /**** ADD CODE HERE ****/
  Card cur = head;
  for (int i=0; i<numOfCards; i++) {
   if (cur instanceof Joker) {
    if (((Joker) cur).redOrBlack.equals(color)) {
     return (Joker) cur;
    }
   }
   cur = cur.next;
  }
  return null;
 }

 /*
  * TODO: Moved the specified Card, p positions down the deck. You can 
  * assume that the input Card does belong to the deck (hence the deck is
  * not empty). This method runs in O(p).
  */
 public void moveCard(Card c, int p) {
  /**** ADD CODE HERE ****/
  if(p != 0) {
   Card card = c;
   c.prev.next = c.next;
   c.next.prev = c.prev;
   for(int i = 0; i < p; i++) {
    card = card.next;
   }
   c.prev = card;
   card.next.prev = c;
   c.next = card.next;
   card.next = c;
  }
 }

 /*
  * TODO: Performs a triple cut on the deck using the two input cards. You 
  * can assume that the input cards belong to the deck and the first one is 
  * nearest to the top of the deck. This method runs in O(1)
  */
 public void tripleCut(Card firstCard, Card secondCard) {
  /**** ADD CODE HERE ****/
  Card temp;
  if(secondCard != head.prev) {
   temp = secondCard.next;
  }
  else {
   temp = firstCard;
  }
  if(firstCard != head) {
   Card firstFront = head;
   Card firstLast = firstCard.prev;
   firstFront.prev.next = firstCard;
   firstCard.prev = firstFront.prev;
   secondCard.next = firstFront;
   firstFront.prev = secondCard;
   firstLast.next = temp;
   temp.prev = firstLast;
  }
  head = temp;
 }

 /*
  * TODO: Performs a count cut on the deck. Note that if the value of the 
  * bottom card is equal to a multiple of the number of cards in the deck, 
  * then the method should not do anything. This method runs in O(n).
  */
 public void countCut() {
  /**** ADD CODE HERE ****/
  Card bottomCard = head.prev;
  int value = bottomCard.getValue();
  if (value % numOfCards == 0 || value == numOfCards - 1) {
   return;
  }
  if (value > numOfCards) {
   value = numOfCards - value;
  }

  Card firstCard = head;
  Card cur = head;

  for (int i=1; i<value; i++) {
   cur = cur.next;
  }
  Card secondCard = cur;
  Card tempPrev = bottomCard.prev;
  Card newHead = secondCard.next;

  secondCard.next = bottomCard;
  bottomCard.prev = secondCard;
  firstCard.prev = tempPrev;
  tempPrev.next = firstCard;
  head = newHead;
  newHead.prev = bottomCard;
 }

 /*
  * TODO: Returns the card that can be found by looking at the value of the 
  * card on the top of the deck, and counting down that many cards. If the 
  * card found is a Joker, then the method returns null, otherwise it returns
  * the Card found. This method runs in O(n).
  */
 public Card lookUpCard() {
  /**** ADD CODE HERE ****/
  int value = head.getValue();
  Card cur = head;

  for (int i=0; i<value; i++) {
   cur = cur.next;
  }
  if (cur instanceof Joker) {
   return null;
  }
  return cur;
 }

 /*
  * TODO: Uses the Solitaire algorithm to generate one value for the keystream 
  * using this deck. This method runs in O(n).
  */
 public int generateNextKeystreamValue() {
  /**** ADD CODE HERE ****/
  Joker red = this.locateJoker("red");
  Joker black = this.locateJoker("black");

  //1. Locate red, and move it one card down
  moveCard(red,1);

  //2. Locate black and move it two down
  moveCard(black,2);

  //3. Triple cut
  //Find top joker
  Card top = this.head;
  while(!(top instanceof Joker)) top = top.next;

  //Triple cut
  if(top == red) {
   this.tripleCut(red,black);
  }
  else tripleCut(black,red);


  //4. Countcut
  countCut();

  //5. Look up Card

  Card keystreamCard = lookUpCard();

  if (keystreamCard == null) {
   return generateNextKeystreamValue();
  }

  return keystreamCard.getValue();
  //return 0;
 }


 public abstract class Card { 
  public Card next;
  public Card prev;

  public abstract Card getCopy();
  public abstract int getValue();

 }

 public class PlayingCard extends Card {
  public String suit;
  public int rank;

  public PlayingCard(String s, int r) {
   this.suit = s.toLowerCase();
   this.rank = r;
  }

  public String toString() {
   String info = "";
   if (this.rank == 1) {
    //info += "Ace";
    info += "A";
   } else if (this.rank > 10) {
    String[] cards = {"Jack", "Queen", "King"};
    //info += cards[this.rank - 11];
    info += cards[this.rank - 11].charAt(0);
   } else {
    info += this.rank;
   }
   //info += " of " + this.suit;
   info = (info + this.suit.charAt(0)).toUpperCase();
   return info;
  }

  public PlayingCard getCopy() {
   return new PlayingCard(this.suit, this.rank);   
  }

  public int getValue() {
   int i;
   for (i = 0; i < suitsInOrder.length; i++) {
    if (this.suit.equals(suitsInOrder[i]))
     break;
   }

   return this.rank + 13*i;
  }

 }

 public class Joker extends Card{
  public String redOrBlack;

  public Joker(String c) {
   if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black")) 
    throw new IllegalArgumentException("Jokers can only be red or black"); 

   this.redOrBlack = c.toLowerCase();
  }

  public String toString() {
   //return this.redOrBlack + " Joker";
   return (this.redOrBlack.charAt(0) + "J").toUpperCase();
  }

  public Joker getCopy() {
   return new Joker(this.redOrBlack);
  }

  public int getValue() {
   return numOfCards - 1;
  }

  public String getColor() {
   return this.redOrBlack;
  }
 }

 //Daniel's code
 private void printDeck()
 {
  Card currentCard = head;
  System.out.println("Previous\tCurrent\t\t\tNext ");
  for(int cardIndex = 0; cardIndex < numOfCards; cardIndex++)
  {
   System.out.println(currentCard.prev + " <--------- " + currentCard + " ---------> " + currentCard.next + ", Values are: " +currentCard.prev.getValue() + " and " + currentCard.getValue() + " and " + currentCard.next.getValue());
   currentCard = currentCard.next;
  }

  System.out.println("Number of cards: " + numOfCards);
 }

}
