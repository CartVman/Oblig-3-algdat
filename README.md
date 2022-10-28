# Obligatorisk oppgave 3 i Algoritmer og Datastrukturer

Denne oppgaven er en innlevering i Algoritmer og Datastrukturer. 
Oppgaven er levert av følgende student:
* Cart Valderama, s364539, s364539@oslomet.no


# Oppgavebeskrivelse

Oppgave 1
The code is from the compendium. it creates a binarysearch tree. every value that is going to be added is being
checked to place it in the right position. Using the while loop, it checks every node to see where to put 
the values. inside the loop, there is a comparator to know if the value is bigger or smaller than the current
node (if less than zero, go left, if not, then go right). the new value will have its own node and will be position
its correct place by using if statements and comparator. (Programkode 5.2 3a, compendium). the new nodes that are being created
uses a constructor to add the parent for that new node.

Oppgave 2
The code is a solution from the compendium. It starts with a node that is equal to root and loops until the end using while-loop.
inside the while-loop, there is a comparator that compares parameter value and node root. the result from comparator will
show where the loop will go (left or right, left if the result < 0, and right if result > 0). if the result from comparator is
equal to zero then increase the counter antall by 1. the method will return the antall counter.

Oppgave 3
The førstepostorden method returns a node that has no children node. a while-loop is created to loop around the nodes and 
find the first postorder node. inside the while-loop, there are if-statements that will direct the loop to the correct direction.
if left child of the current node is not null then continue going left, and if the right child of the current is not null
equal to null then go right, but if both child are equal to null then return the current node.

Using the explanation from the compendium. there conditions for returning the next postorder value.
these are the interpretation of the conditions to a code perspective:
- return null if parent is null
- if p is the right child of its parent, the parent is the next node.
- if p is the left child of its parent, and it is the only child, its parent is the next node or if the 
node is not the only child, then check the first postorder node of the parent of that node.

Oppgave 4
The postorden iterative method uses a while-loop. first is to create a node and initialize it first
postorder value(using the førstpostorden method). inside the loop another method is being called(the nestepostorden method)
to get the next postorder node.

the postorden recursive method has the basic code for a postorder recursive. first is the base case, an if-statement is created to
stop the function calling itself when the node is equal to null. then call the first recursive call and inside is the node 
parameter that is used for going to the left. the second recursive call has a node that is used for going to the right.
after the two recursive calls, it will be pointed at a postorder node which can be return or printed.

Oppgave 5
The serialize method uses a queue to store all the nodes and arraylist to get the values from the nodes inside the queue.
a whilie-loop is used to loop until the queue is empty. inside the loop, the last value of the 
queue will be added to the arraylist. there are if-statements that are used for storing the nodes in a level order.
it will return an arraylist.

The deserialize method creates an object to be used for calling a method inside its class. Using a for-each loop the data
parameter will be stored one by one to the object that is created from the start. it will be stored in a level order using the method
legginn. it will return a binarysearch tree level order .

Oppgave 6
The method fjern comes from the compendium. the additional code that is added is on line 128. if the child node is not
not equal to null, then its new parents will be the parent of the node that is going to be removed.
The fjernalle method uses another method to count all the similar values inside the list and will return it
at the end. it uses a while loop and the condition for that loop uses a method called inneholder which
will return a boolean (return true if there still a similar value that exist in a list, then false if there is not).
The nullstill method uses førstepostorder method to gets its starting node. it has a while-loop that will 
run until the size of the list is equal to zero. inside the loop every values that are equal to the parameter
value is going to be removed using the fjern method. the nestepostorden method is used to select the nest postorder node that
will be removed. in the en all the nodes inside the list will be removed.