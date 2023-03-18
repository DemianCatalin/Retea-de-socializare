package com.example.reteadesocializare.graph;

import com.example.reteadesocializare.utils.PairEntity;
import com.example.reteadesocializare.exceptions.DuplicatedElementException;

import java.util.*;

public class Graf<Node, Edge extends PairEntity<Node, Node>> implements Graph<Node, Edge> {
    private final Map<Node, Set<Edge>> G;
    private final Map<Node, Boolean> deleted;

    public Graf(){
        G = new HashMap<>();
        deleted = new HashMap<>();
    }

    public Boolean hasNode(Node nod){
        return G.containsKey(nod) && (!deleted.containsKey(nod) || !deleted.get(nod));
    }

    @Override
    public void addNode(Node nod) throws DuplicatedElementException {
        if(!G.containsKey(nod)){
            G.put(nod, new HashSet<>());
            deleted.put(nod, false);
        }
        else if(deleted.containsKey(nod) && deleted.get(nod))
            deleted.put(nod, false);
        //else throw new DuplicatedElementException("Nod duplicat!");
    }

    @Override
    public void addEdge(Edge muchie) throws DuplicatedElementException {
        Node nod1 = muchie.getFirst();
        Node nod2 = muchie.getSecond();
        if(!hasNode(nod1)) addNode(nod1);
        if(!hasNode(nod2)) addNode(nod2);
        G.get(nod1).add(muchie);
        G.get(nod2).add(muchie);
    }

    public Set<Node> getAdiacents(Node nod){
        Set<Node> vecini = new HashSet<>();
        G.get(nod).forEach(muchie -> {
            Node nod1;
            if(muchie.getFirst().equals(nod))
                nod1 = muchie.getSecond();
            else nod1 = muchie.getFirst();
            if(hasNode(nod1))
                vecini.add(nod1);
        });
        return vecini;
    }

    public Set<Edge> getAdiacentEdges(Node nod){
        Set<Edge> rez = new HashSet<>();
        G.get(nod).forEach(muchie -> {
            Node nod1;
            if(muchie.getFirst().equals(nod))
                nod1 = muchie.getSecond();
            else nod1 = muchie.getFirst();
            if(hasNode(nod1))
                rez.add(muchie);
        });
        return rez;
    }

    @Override
    public Set<Node> getNodes(){
        return G.keySet();
    }

    public Graf<Node, Edge> component(Node source){
        Graf<Node, Edge> graf = new Graf<>();
        Queue<Node> queue = new LinkedList<>();
        Map<Node, Boolean> visited = new HashMap<>();
        for(Node nod : G.keySet()) {
            visited.put(nod, false);
        }
        queue.add(source);
        graf.addNode(source);
        while (!queue.isEmpty()){
            Node curent = queue.poll();
            if(curent != null && !visited.get(curent)){
                visited.put(curent, true);
                for(Edge edge : getAdiacentEdges(curent)){
                    Node nod;
                    if(edge.getFirst().equals(curent))
                        nod = edge.getSecond();
                    else
                        nod = edge.getFirst();
                    if(!visited.get(nod)){
                        queue.add(nod);
                        graf.addEdge(edge);
                    }
                }
            }
        }
        return graf;
    }

    public List<Graf<Node, Edge>> components(){
        List<Graf<Node, Edge>> grafuri = new LinkedList<>();
        Map<Node, Boolean> visited = new HashMap<>();
        for(Node nod : G.keySet()) {
            visited.put(nod, false);
        }
        for(Node nod : G.keySet()){
            if(!visited.get(nod)){
                Graf<Node, Edge> graf = component(nod);
                grafuri.add(graf);
                graf.getNodes().forEach(n -> visited.put(n, true));
            }
        }
        return grafuri;
    }
}
