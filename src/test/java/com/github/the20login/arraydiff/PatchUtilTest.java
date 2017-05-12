package com.github.the20login.arraydiff;

import com.github.the20login.arraydiff.patch.Patch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.google.common.truth.Truth.assertThat;

@RunWith(value = Parameterized.class)
public class PatchUtilTest {
    @Parameterized.Parameter(value = 0)
    public int[] initial;
    @Parameterized.Parameter(value = 1)
    public int[] target;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {
                        new int[]{1, 2, 3, 4, 5, 6},
                        new int[]{1, 2, 5, 6, 7, 8}
                },
                {
                        new int[]{1, 2, 5, 6, 7, 8},
                        new int[]{1, 2, 7, 8}
                },
                {
                        new int[]{1, 2, 7, 8},
                        new int[]{1, 2, 9, 10, 7, 8}
                },
                {
                        new int[]{1, 2, 3, 4, 5, 6},
                        new int[]{7, 8, 9, 10, 11, 12}
                }
        });
    }

    @Test
    public void createPatch() throws Exception {
        Patch patch = PatchUtil.createPatch(initial, target);
        int[] copy = patch.apply(initial);
        assertThat(copy).isEqualTo(target);
    }

}