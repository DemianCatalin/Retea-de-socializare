package com.example.reteadesocializare.graph;

import com.example.reteadesocializare.utils.Pair;
import com.example.reteadesocializare.utils.PairEntity;

import java.util.*;

public class GraphArgorithms {
    protected static <Node, Edge extends PairEntity<Node, Node>> void dfs(Graf<Node, Edge> graf, Node source, Map<Node, Boolean> visited, Map<Node, Node> parent, Map<Node, Integer> distance){
        Stack<Node> S = new Stack<>();
        S.push(source);
        visited.put(source, true);
        parent.put(source, null);
        distance.put(source, 0);
        while(!S.isEmpty()){
            Node nod = S.pop();
            for(Node vecin : graf.getAdiacents(nod)){
                if(!visited.get(vecin)){
                    visited.put(vecin, true);
                    parent.put(vecin, nod);
                    distance.put(vecin, distance.get(nod) + 1);
                    S.push(vecin);
                }
            }
        }
    }

    public static <Nod, Muchie extends PairEntity<Nod, Nod>> Pair<Graf<Nod, Muchie>, Integer> componentWithLongestPath(Graf<Nod, Muchie> graf){
        int dmax = -1;
        Nod source = null;
        Map<Nod, Nod> parent = new HashMap<>();
        Map<Nod, Boolean> visited = new HashMap<>();
        Map<Nod, Integer> distance = new HashMap<>();
        for(Nod nod : graf.getNodes()){
            visited.put(nod, false);
            parent.put(nod, null);
            distance.put(nod, 0);
        }
        for(Nod nod : graf.getNodes()){
            dfs(graf, nod, visited, parent, distance);
            int d = distance.values().stream().max(Integer::compareTo).get();
            if(d > dmax){
                dmax = d;
                source = nod;
            }
            for(Nod nod2 : graf.getNodes()){
                visited.put(nod2, false);
                parent.put(nod2, null);
                distance.put(nod2, 0);
            }
        }
        return new Pair<>(graf.component(source), dmax);
    }
}
