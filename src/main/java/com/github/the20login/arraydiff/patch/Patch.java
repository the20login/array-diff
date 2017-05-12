package com.github.the20login.arraydiff.patch;

import java.util.ArrayList;
import java.util.List;

public class Patch {
    private final List<PatchOperation> operations = new ArrayList<>();
    private final int targetSize;

    public Patch(int targetSize) {
        this.targetSize = targetSize;
    }

    public void insert(int index, int[] value) {
        operations.add(new PatchOperation(index, value));
    }

    public void remove(int index, int count) {
        operations.add(new PatchOperation(index, count));
    }

    public int[] apply(int[] initial) {
        int[] target = new int[targetSize];

        int initialIndex = 0;
        int targetIndex = 0;
        for (PatchOperation operation : operations) {
            for (; initialIndex < operation.index; initialIndex++, targetIndex++) {
                target[targetIndex] = initial[initialIndex];
            }

            if (operation.type.equals(OpType.add)) {
                for (int i : operation.value) {
                    target[targetIndex] = i;
                    targetIndex++;
                }
            } else {
                initialIndex += operation.count;
            }
        }

        for (; initialIndex < initial.length; initialIndex++, targetIndex++) {
            target[targetIndex] = initial[initialIndex];
        }
        return target;
    }

    private enum OpType {
        add,
        remove
    }

    private static class PatchOperation {
        final OpType type;
        final int index;
        final int[] value;
        final int count;


        PatchOperation(int index, int[] value) {
            this.type = OpType.add;
            this.index = index;
            this.value = value;
            this.count = value.length;
        }

        PatchOperation(int index, int count) {
            this.type = OpType.remove;
            this.index = index;
            this.count = count;
            this.value = null;
        }
    }
}
