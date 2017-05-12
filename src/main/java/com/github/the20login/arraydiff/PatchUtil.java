package com.github.the20login.arraydiff;

import com.github.the20login.arraydiff.patch.Patch;

import java.util.Arrays;

public abstract class PatchUtil {
    //TODO: common prefix, common suffix
    public static Patch createPatch(int[] initial, int[] target) {
        Patch patch = new Patch(target.length);
        int initialIndex = 0, targetIndex = 0;
        for (; ; ) {
            if (initialIndex == initial.length) {
                if (targetIndex < target.length)
                    patch.insert(initialIndex, Arrays.copyOfRange(target, targetIndex, target.length));
                break;
            } else if (targetIndex == target.length) {
                if (initialIndex < initial.length)
                    patch.remove(initialIndex, initial.length - initialIndex);
                break;
            }
            int initialItem = initial[initialIndex];
            int targetItem = target[targetIndex];

            //equal part
            if (initialItem == targetItem) {
                initialIndex++;
                targetIndex++;
            } else {
                int initialDiffIndex = initialIndex + 1;
                int targetDiffIndex = targetIndex + 1;

                for (; ; initialDiffIndex++, targetDiffIndex++) {
                    if (initialDiffIndex == initial.length || targetDiffIndex == target.length) {
                        patch.remove(initialIndex, initial.length - initialIndex);
                        patch.insert(initialIndex, Arrays.copyOfRange(target, targetIndex, target.length));
                        return patch;
                    } else if (initial[initialDiffIndex] == targetItem) {
                        patch.remove(initialIndex, initialDiffIndex - initialIndex);
                        initialIndex = initialDiffIndex;
                        break;
                    } else if (target[targetDiffIndex] == initialItem) {
                        patch.insert(initialIndex, Arrays.copyOfRange(target, targetIndex, targetDiffIndex));
                        targetIndex = targetDiffIndex;
                        break;
                    }
                }
            }
        }

        return patch;
    }
}
