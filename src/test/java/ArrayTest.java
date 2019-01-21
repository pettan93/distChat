import org.jetbrains.annotations.TestOnly;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayTest {


    @Test
    public void arrayTest(){


        ArrayList nodes = new ArrayList<Integer>();
        nodes.addAll(Arrays.asList(1,2,3,4,5));


        ArrayList deadNodes = new ArrayList<Integer>();
        deadNodes.addAll(Arrays.asList(1,2));


        nodes.removeAll(deadNodes);

        System.out.println(nodes.get(0));



    }



}
