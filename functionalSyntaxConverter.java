package Intro;


// Supported constructs are union, intersection, equivalance and subclass.


import java.io.File;                                                                                // importing io file
import java.util.ArrayList;                                                                         // imporint file for ArrayList    

import org.semanticweb.owlapi.apibinding.OWLManager;                                                // importing owlapi files
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class MAIN {

    static String[] split(String s) {                                                               // Function for splitting a compound axiom into two halves
        if (s.charAt(0) == '<') {                                                                   // When the first character starts with <
            int ind = s.indexOf('>');                                                               // finding the index of >
            String[] arr = new String[2];
            arr[0] = s.substring(0, ind + 1);
            arr[1] = s.substring(ind + 2);
            return arr;
        }
        int open = 1;                                                                               
        int close = 0;
        int ind = s.indexOf('(') + 1;                                                               // finding the index of '('
        while (open != close) {                                                                     // Checking balanced parentheses condition
            if (s.charAt(ind) == '(') {
                open++;                                                                             // incrementing open
            }
            else if (s.charAt(ind) == ')') {
                close++;                                                                            // incrementing close
            }
            ind++;
        }
        String[] arr = new String[2];
        arr[0] = s.substring(0, ind);
        arr[1] = s.substring(ind + 1);
        return arr;
    }
    
    
    static String refine(String s) {                                                                // function for removing the common prefix from string.
        int ind = s.indexOf('#');                                                                   // finding the index of hash
        return s.substring(ind + 1, s.length() - 1);
    }
    
    
    static ArrayList<String> arrLst;
    static void convert(String s) {                                                                 // recursive function for string parsing
        if (s.charAt(0) == '<') {                                                                   // base case
            arrLst.add(refine(s));
        }
        else if (s.charAt(0) == 'E') {                                                              // When the functional syntax has equivalence
            int ind1 = s.indexOf('(') + 1;
            int ind2 = s.length() - 2;
            s = s.substring(ind1, ind2 + 1);
            String[] arr = split(s);                                                                // splitting the string  
            convert(arr[0]);                                                                        // recursively calling for left half  
            arrLst.add("≡");                                                                        // adding equivalance symbol to arrLst
            convert(arr[1]);                                                                        // recursively calling for right half
        }
        else if (s.charAt(0) == 'S') {                                                              // When the functional syntax has subclass
            int ind1 = s.indexOf('(') + 1;                                                          
            int ind2 = s.length() - 2;
            s = s.substring(ind1, ind2 + 1);
            String[] arr = split(s);                                                                // splitting the string  
            convert(arr[0]);                                                                        // recursively calling for left half  
            arrLst.add("⊑");                                                                         // adding subclass symbol to arrLst
            convert(arr[1]);                                                                        // recursively calling for right half
        }
        else if (s.charAt(0) == 'O' && s.charAt(6) == 'U') {                                        // When the functional syntax has union
            int ind1 = s.indexOf('(') + 1;
            int ind2 = s.length() - 2;
            s = s.substring(ind1, ind2 + 1);
            String[] arr = split(s);                                                                // splitting the string 
            convert(arr[0]);                                                                        // recursively calling for left half  
            arrLst.add("⊔");                                                                         // adding union symbol to arrLst
            convert(arr[1]);                                                                         // recursively calling for right half
        }
        else if (s.charAt(0) == 'O' && s.charAt(6) == 'I') {                                        // When the functional syntax has intersection
            int ind1 = s.indexOf('(') + 1;
            int ind2 = s.length() - 2;
            s = s.substring(ind1, ind2 + 1);
            String[] arr = split(s);                                                                // splitting the string 
            convert(arr[0]);                                                                        // recursively calling for left half  
            arrLst.add("⊓");                                                                        // adding intersection symbol to arrLst
            convert(arr[1]);                                                                        // recursively calling for right half
        }
    }
    
    static String callConvert(String s) {                                                           // helper method (to convert functional syntax to DL syntax)
        arrLst = new ArrayList();                                                                   // initializes arrLst
        convert(s);
        String res = arrLst.get(0);
        for (int i = 1; i < arrLst.size(); i++) {
            res = res + " " + arrLst.get(i);                                                        // appending to res
        }
        return res;                                                                                 // returning the formed string        
    }
    
    public static void main(String[] args) throws OWLOntologyCreationException {
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();                           // creating OWLOntologyManager
        try {
            OWLOntology owl=manager.loadOntologyFromOntologyDocument(new File("myOntology.owl"));   // loading the axioms from the file
          
            for (OWLAxiom axiom : owl.getAxioms()) {                                                // Iterating for each axiom    
              String s = axiom.toString();
              if (s.charAt(0) == 'D') {                                                             // ignoring the declarative statements
                  continue;
              }
              System.out.println(s);                                                                // printing functional syntax       
              System.out.println(callConvert(s));                                                   // calling helper method to convert functional syntax to DL syntax
              System.out.println();
            }
            
        }
        catch (Exception e) {                                                                       // checking whether the axioms are valid or not.
            System.out.println("The given axioms are not valid.");                                  // printing error message.
        }
        

    }

}
