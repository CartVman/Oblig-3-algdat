package no.oslomet.cs.algdat.Oblig3;

import java.util.*;

public class SBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public SBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }
        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, parent = null;               // root starter i roten
        int cmp = 0;                                    // hjelpevariabel

        while (p != null)                                // fortsetter til root er ute av treet
        {
            parent = p;                                 // q er forelder til root
            cmp = comp.compare(verdi,p.verdi);           // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter root
        }

        // root er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<T>(verdi,parent);                   // creates a new node and points to the parent node

        if (parent == null) {
            rot = p;                  // root blir rotnode
        } else if (cmp < 0) {
            parent.venstre = p;         // venstre barn til q
        } else {
            parent.høyre = p;                        // høyre barn til q
        }

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    public boolean fjern(T verdi) {
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) { q = p; p = p.venstre; }      // går til venstre
            else if (cmp > 0) { q = p; p = p.høyre; }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn

            if (b != null) {
                b.forelder = q;
            }

            if (p == rot) {
                rot = b;
            }
            else if (p == q.venstre) {
                q.venstre = b;
            }
            else {
                q.høyre = b;
            }
        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) s.venstre = r.høyre;
            else s.høyre = r.høyre;
        }

        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi) {
        int antall = antall(verdi);           // gets the total number of similar values inside a list.

        while (inneholder(verdi)) {         // loops until the last similar value of the parameter verdi
            fjern(verdi);                   // removes every values that are equal to parameter verdi
        }

        return antall;                      // return total number of similar values inside a list
    }

    public int antall(T verdi) {
        Node<T> p = rot;                // creates a node to loop
        int antallVerdi = 0;            // counts the duplicate number

        while (p != null)               // loops until the list is empty or pointing to null
        {
            int cmp = comp.compare(verdi,p.verdi);          // compare to get the direction
            if (cmp < 0) {
                p = p.venstre;                              // go left
            } else {
                if (cmp == 0) {
                    antallVerdi++;
                }
                p = p.høyre;                                // go right
            }
        }
        return antallVerdi;
    }

    public void nullstill() {
        Node <T> root = førstePostorden(rot);               // find the first postorder node

        while (antall != 0) {                               // loops until it is empty
            if (root != null) {
                fjern(root.verdi);                          // remove the current postorder value
                root = nestePostorden(root);                // move to the next postorder node
            }
            antall--;                                       // reduce size
        }
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {
        if (p == null) {
            return null;                // return if p is equal to null
        }

        while (true) {                  // this loop finds the first node that has no child
            if (p.venstre != null) {
                p = p.venstre;
            } else if(p.høyre != null){
                p = p.høyre;
            } else {
                return p;               // return the node that has no child
            }
        }
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        Node <T> parent = p.forelder;           // create a node parent

        if (parent == null) {
            return null;                        // return null if parent is null
        }

        if (parent.høyre== p) {
            p = parent;                         // if p is the right child of its parent, the parent is the next node
        } else if (parent.venstre == p) {       // if p is the left child of its parent
            if (parent.høyre == null) {
                p = parent;                     // and it is the only child, its parent is the next node
            } else {
                p = førstePostorden(parent.høyre); // if the node is not the only child, then check the first postorder node of the parent of that node.
            }
        }
        return p;
    }

    public void postorden(Oppgave<? super T> oppgave) {
        if (tom()) {
            return;                             // return if the list is empty
        }

        Node <T> temp = førstePostorden(rot);       // create a root node
        while (temp != null) {                      // loop until the temp is quel to null (empty list).
            oppgave.utførOppgave(temp.verdi);
            temp = nestePostorden(temp);            // find the next postorder node.
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if (p == null) {
            return;                         // return if p is null
        }

        postordenRecursive(p.venstre,oppgave);      // calls the function repeatedly to transverse p to the left until it is equal to null
        postordenRecursive(p.høyre,oppgave);        // calls the function repeatedly to transverse p to the right until the p node is equal to null

        oppgave.utførOppgave(p.verdi);
    }

    public ArrayList<T> serialize() {
        ArrayDeque<Node<T>>queue = new ArrayDeque<>();       // Create a queue for storing the nodes
        queue.addLast(rot);
        ArrayList <T> list = new ArrayList<>();           // use for storing the values from the queue

        while (!queue.isEmpty()) {                           // loops until the list queue is empty
            Node<T> p = queue.pop();                         // to get the last node from the list
            list.add(p.verdi);                              // then adding it to the arraylist result

            if (p.venstre != null) {
                queue.addLast(p.venstre);                    // adding the value to the left
            }
            if (p.høyre != null) {
                queue.addLast(p.høyre);                      // storing the value to the right
            }
        }
        return list;                                      // return the serialized tree
    }

    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        SBinTre <K> tree = new SBinTre<>(c);            // create an object to call the other methods

        for (K value : data) {                          // loops every value that comes from the parameter data
            tree.leggInn(value);                        // inserting the values inside the object in level order
        }

        return tree;                                    // return the level ordered tree.
    }
} // ObligSBinTre
