package de.jonasfrey.admintools;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Jonas Frey
 * @version 1.0, 07.08.17
 */
public class PlaytimeComparator implements Comparator<String> {
    
    Map<String, Integer> playtimes;
    
    public PlaytimeComparator(Map<String, Integer> playtimes) {
        this.playtimes = playtimes;
    }


    @Override
    public int compare(String o1, String o2) {
        return playtimes.get(o1).compareTo(playtimes.get(o2));
    }
}
