/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4;

/**
 * @author aliahmed
 */

import ecs100.UI;
import ecs100.UIFileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Lab4 {
    Vector<Node> nodes = new Vector<Node>();
    HashMap<Node,HashMap<String,List<Node>>> routePath=new HashMap<>();
    HashMap<Node,HashMap<Node,Integer>> routeCost= new HashMap<>();
    public Lab4() {
        UI.addButton("load map", this::load);
        UI.addButton("Draw Nodes", this::draw);
        UI.addButton("Start", this::start);
        UI.addButton("Change a cost",this::changeCost);
        UI.addButton("Choose node to show how data is routed",this::showRoutate);
    }
    public void showRoutate(){
        start();
        draw();
        UI.clearText();
        UI.println("Notice: please input uppercase character.");
        String startNode_Name =UI.askString("Type in [Start node] of the line: (eg, A, B, ..)");
        String toNode_Name = UI.askString("Type in [End node] of the line: (eg, C, D, ..)");

        Node startNode = findNode(startNode_Name);
        Node toNode = findNode(toNode_Name);
        boolean isValid=startNode!=null&&toNode!=null;
        if(!isValid){
            UI.println("Error: Please input valid node!");
            return;
        }
        startNode.initialise(nodes);
        UI.clearText();
        HashMap<String,List<Node>> pathMap =routePath.get(startNode);
        List<Node> toPath = pathMap.get(toNode_Name);
        UI.println("From: "+startNode_Name+"   To: "+toNode.getName());
        UI.print("how data routed: \n"+startNode.getName()+"(parentNode, cost)");

        for(Node n:toPath){
           if(n.getPreviouseParent()==null){
               UI.print("->"+n.getName()+"(null,"+n.getPreviouseCost()+")");
           }else{
               UI.print("->"+n.getName()+"("+n.getPreviouseParent().getName()+","+n.getPreviouseCost()+")");
           }

        }
        UI.println();
        HashMap<Node,Integer> costMap= routeCost.get(startNode);
        UI.println("total cost: "+costMap.get(toNode));



    }
    /**
     * This method is used to print the router table and find shortest path
     * */
    public void start() {
        long start =System.currentTimeMillis();
        int [][]costTable =new int[nodes.size()][nodes.size()];

        int i=0;

        for (Node n : this.nodes) {
            n.initialise(nodes);

            HashMap<String,List<Node>> tempPath= n.getPath();
            HashMap<Node,Integer> tempCost = n.getPathCost();
            routePath.put(n,tempPath);
            routeCost.put(n,tempCost);
            costTable[i++]=n.getCostArray();
        }



        printTable(costTable);
        long end = System.currentTimeMillis();


        UI.println("Time Cost: "+(end-start)/1000.0+" s");
    }
    /**
     * This method is used to return the node by passing the node name
     * @param name
     * */
    public Node findNode(String name){
        for(Node node:nodes){
            if(node.getName().equals(name)){
                return node;
            }
        }
        return null;
    }
    /***
     *This method is used to check if node1 and node2 are neighbour with each other
     * @param node1
     * @param node2
     * */
    public boolean isNeighbour(Node node1,Node node2){
        Set<String> keys = node1.getNeighbours().keySet();
        for(String key:keys){
            if(key.equals(node2.getName())){
                return true;
            }
        }
        return false;
    }


    /**
     * This method is used for user to input a new cost in one of line of the diagram in order to change the cost of the line
     * */
    public void changeCost(){
        UI.clearText();
        UI.println("Notice: please input uppercase character.");
        String startNode_Name =UI.askString("Type in [Start node] of the line: (eg, A, B, ..)");
        String toNode_Name = UI.askString("Type in [End node] of the line: (eg, C, D, ..)");

        Node startNode = findNode(startNode_Name);
        Node toNode = findNode(toNode_Name);
        boolean isValid=startNode!=null&&toNode!=null;
        if(!isValid){
            UI.println("Error: Please input valid node!");
            return;}
        if(!isNeighbour(startNode,toNode)){
            UI.println("Error: There is not a line between these two nodes!");
            return;
        }

        int newCost= UI.askInt("input the new cost between the two nodes: ");
        if(newCost<=0){
            UI.println("Error: Please input a valid cost");
            return;
        }
        for(Node node:nodes){
            if(node.getName().equals(startNode_Name)){
                node.addNeighbour(toNode_Name,newCost);
            }else if(node.getName().equals(toNode_Name)){
                node.addNeighbour(startNode_Name,newCost);
            }
        }
        UI.clearText();
        UI.clearGraphics();
        UI.println("----------------repaint right panel ---------");
        UI.println("----------------reprint the table-------------");
        UI.println();
        draw();
        start();

    }


    public void printTable(int[][] costTable){
        UI.println("Routing table for all nodes");
        UI.print("  |");
        for(Node node:nodes){
            UI.print( node.getName()+"  ");
        }
        UI.println();
        for(int i=0;i<nodes.size()+16;i++){
            UI.print("-");
        }
        UI.println();
        for(int i=0;i<nodes.size();i++){
            UI.print(" "+nodes.get(i).getName()+"|");
            for(int col=0;col<nodes.size();col++){
                UI.print(costTable[i][col]+"  ");
            }
            UI.println();

        }
        for(int i=0;i<nodes.size()+16;i++){
            UI.print("-");
        }
        UI.println();

    }
    /**
     * This method is used to load map file into nodes
     * */
    public void load() {
        try {
            Scanner scan = new Scanner(new File(UIFileChooser.open("Select Map File")));
            while (scan.hasNext()) {
                String n = scan.next();
                int x = scan.nextInt();
                int y = scan.nextInt();
                Node node = new Node(n, x, y);
                int count = scan.nextInt(); // the number of neighbouring nodes
                for (int i = 0; i < count; i++)
                    node.addNeighbour(scan.next(), scan.nextInt());

                this.nodes.add(node);
            }

            scan.close();
        } catch (IOException e) {
            UI.println("File Failure: " + e);
        }
    }
    /**
     * According to the map files, draw the diagram.
     * */
    public void draw() {
        for (Node n : this.nodes) {
            UI.setColor(Color.green);
            UI.fillOval(n.getxPos(), n.getyPos(), 40, 40);
            UI.setColor(Color.blue);
            UI.drawString(n.getName(), n.getxPos() + 5, n.getyPos() + 22);

            UI.setColor(Color.red);
            // loop on all neighbours
            Set<String> keys = n.getNeighbours().keySet();
            for (String s : keys) {
                //Search in the list of nodes for this node wth name "s"
                Node neighbour = null;
                for (int i = 0; i < this.nodes.size(); i++) {
                    if (this.nodes.get(i).getName().equals(s)) {
                        neighbour = this.nodes.get(i);
                        break;
                    }
                }

                if (neighbour != null) // there is a neighbour
                {
                    UI.drawLine(n.getxPos() + 20, n.getyPos() + 20, neighbour.getxPos() + 20, neighbour.getyPos() + 20);
                    int offX= neighbour.getxPos()-n.getxPos();
                    int offY = neighbour.getyPos()-n.getyPos();
                    int midX = offX>0?n.getxPos()+offX/2:neighbour.getxPos()-offX/2;
                    int midY= offY>0?n.getyPos()+offY/2:neighbour.getyPos()-offY/2;
                    UI.drawString(" "+n.getNeighbours().get(neighbour.getName()),20+midX,20+midY);
                    //this part is used to show the cost of every line.
                }


            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Lab4();
    }

}
