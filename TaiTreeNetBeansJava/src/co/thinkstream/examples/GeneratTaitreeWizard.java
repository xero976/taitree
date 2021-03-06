/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.thinkstream.examples;

import co.thinkstream.main.TaiLogicUnit;
import co.thinkstream.struct.TaiNode;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.jfr.events.FileWriteEvent;

/**
 *
 * @author test
 */
public class GeneratTaitreeWizard {

    List<TaiNode> tree;

    public GeneratTaitreeWizard() {

        System.out.println("Welcome to the TaiTree Generator.");
        System.out.println("This will generate code for you.");
        System.out.println("Whats the name of the file?");
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");
        tree = new ArrayList<>();
        String file = sc.nextLine();
        System.out.println("Enter your first question:");
        String question = sc.nextLine();
        TaiNode root = new TaiNode();
        root.setId(-1);
        root.setParentId(-2);
        root.setIsEndNode(false);
        root.setQuestion(question);
        tree.add(root);
        recursiveQuestions(root, sc);
        TaiLogicUnit logic = new TaiLogicUnit();
        String jsonData = logic.javaObjectToJSON(tree);
        System.out.println("");
        System.out.println("Writing the following file: ");
        System.out.println(jsonData);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(jsonData);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(GeneratTaitreeWizard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void recursiveQuestions(TaiNode root, Scanner sc) {
        System.out.println("For Question: " + root.getQuestion() + " (" + root.getAnswer() + ")");

        System.out.println("How many answers are there? (" + root.getQuestion() + ")");
        int children = Integer.valueOf(sc.nextLine());
        String solution = "";
        List<TaiNode> qanda = new LinkedList<>();
        for (int i = 0; i < children; i++) {
            TaiNode child = new TaiNode();
            String option = getOption(i);
            System.out.println("Whats the " + option + " possible answer? (" + root.getQuestion() + ")");
            String ans = sc.nextLine();
            System.out.println("Is this an ending solution?");
            boolean isLeaf = getBool(sc);
            child.setIsEndNode(isLeaf);
            child.setAnswer(ans);
            child.setParentId(root.getId());
            if (isLeaf) {
                System.out.println("Enter the solution:");
                solution = sc.nextLine();
                child.setResult(solution);
                tree.add(child);
            } else {
                System.out.println("Enter a question for answer :(" +root.getQuestion()+":"+ child.getAnswer() + ")");
                String question = sc.nextLine();
                child.setQuestion(question);
                tree.add(child);

            }
            qanda.add(child);
        }
        System.out.println("");
        for (int i = 0; i < qanda.size(); i++) {
            TaiNode n = qanda.get(i);
            if (!n.isIsEndNode()) {
                printBranches(n);
                recursiveQuestions(n, sc);
            }
        }

    }

    public static void main(String[] args) {
        new GeneratTaitreeWizard();
    }

    private boolean getBool(Scanner sc) {
        String ans = sc.nextLine();
        if (ans.equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }
    }

    private void printBranches(TaiNode n) {
        TaiLogicUnit lo = new TaiLogicUnit();
        List<TaiNode> path = lo.getPath(n, tree);
        String space = "";
        for (int i = 0; i < path.size(); i++) {
            System.out.println(space + "Q:" + path.get(i).getQuestion() + ",A:" + path.get(i).getAnswer());
            space = space + " ";
        }
    }

    private String getOption(int i) {
        String opt = "";
        switch (i) {
            case 0:
                opt = "first";
                break;
            case 1:
                opt = "second";
                break;
            case 2:
                opt = "third";
                break;
            case 3:
                opt = "fourth";
                break;
            default:
                opt = "another";
                break;
        }
        return opt;
    }
}
