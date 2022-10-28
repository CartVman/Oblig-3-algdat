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

        Node<T> root = rot, parent = null;               // root starter i roten
        int cmp = 0;                                    // hjelpevariabel

        while (root != null)                                // fortsetter til root er ute av treet
        {
            parent = root;                                 // q er forelder til root
            cmp = comp.compare(verdi,root.verdi);           // bruker komparatoren
            root = cmp < 0 ? root.venstre : root.høyre;     // flytter root
        }

        // root er nå null, dvs. ute av treet, q er den siste vi passerte

        root = new Node<T>(verdi,parent);                   // creates a new node and points to the parent node

        if (parent == null) {
            rot = root;                  // root blir rotnode
        } else if (cmp < 0) {
            parent.venstre = root;         // venstre barn til q
        } else {
            parent.høyre = root;                        // høyre barn til q
        }

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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
        ArrayDeque<Node<T>>list = new ArrayDeque<>();
        list.addLast(rot);
        ArrayList <T> result = new ArrayList<>();

        while (!list.isEmpty()) {
            Node<T> p = list.pop();
            result.add(p.verdi);

            if (p.venstre != null) {
                list.addLast(p.venstre);
            }
            if (p.høyre != null) {
                list.addLast(p.høyre);
            }
        }
        return result;

    }

    static <K> SBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        SBinTre <K> temp = new SBinTre<>(c);

        for (K value : data) {
            temp.leggInn(value);
        }

        return temp;
    }
} // ObligSBinTre
