package com.laylib.jintl.provider;

import com.laylib.jintl.entity.SourceIndex;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Source Index Diff
 *
 * @author Lay
 */
public class SourceIndexDiff {

    /**
     * compare difference between two {@link SourceIndex}
     * @param src
     * @param target
     * @return
     */
    public static DiffResult compare(SourceIndex src, SourceIndex target) {
        // create map
        Map<String, Set<Locale>> srcMap = src.getItems().stream().collect(Collectors.toMap(SourceIndex.IndexItem::getTag, SourceIndex.IndexItem::getLocales));
        Map<String, Set<Locale>> targetMap = target.getItems().stream().collect(Collectors.toMap(SourceIndex.IndexItem::getTag, SourceIndex.IndexItem::getLocales));

        // DiffResult
        DiffResult diffResult = new DiffResult();

        if (srcMap.isEmpty()) {
            // all added
            SourceIndex sourceIndex = new SourceIndex();
            sourceIndex.setItems(targetMap.entrySet().stream().map(e -> new SourceIndex.IndexItem(e.getKey(), e.getValue())).collect(Collectors.toList()));
            diffResult.addedIndex = sourceIndex;
            return diffResult;
        }

        if (targetMap.isEmpty()) {
            // remove all
            SourceIndex sourceIndex = new SourceIndex();
            sourceIndex.setItems(srcMap.entrySet().stream().map(e -> new SourceIndex.IndexItem(e.getKey(), e.getValue())).collect(Collectors.toList()));
            diffResult.removedIndex = sourceIndex;
            return diffResult;
        }

        Set<String> combinedTags = new HashSet<>() {
            {
                addAll(srcMap.keySet());
                addAll(targetMap.keySet());
            }
        };

        for (String tag : combinedTags) {
            int flag = 0;
            if (srcMap.containsKey(tag)) {
                flag -= 1;
            }
            if (targetMap.containsKey(tag)) {
                flag += 1;
            }

            switch (flag) {
                case -1: {
                    // only exists in src so remove all locales of this tag
                    SourceIndex.IndexItem indexItem = new SourceIndex.IndexItem(tag, srcMap.get(tag));
                    diffResult.removedIndex.getItems().add(indexItem);
                    break;
                }
                case 1: {
                    // only exists in target so add all locales of this tag
                    SourceIndex.IndexItem indexItem = new SourceIndex.IndexItem(tag, targetMap.get(tag));
                    diffResult.addedIndex.getItems().add(indexItem);
                    break;
                }
                default: {
                    TreeSet<Locale> srcLocales = new TreeSet<>(Comparator.comparing(Locale::toLanguageTag));
                    srcLocales.addAll(srcMap.get(tag));
                    TreeSet<Locale> targetLocales = new TreeSet<>(Comparator.comparing(Locale::toLanguageTag));
                    targetLocales.addAll(targetMap.get(tag));
                    if (srcLocales.toString().equals(targetLocales.toString())) {
                        // no changes
                        continue;
                    }

                    Set<Locale> removedLocales = null;
                    Set<Locale> addedLocales = null;

                    // add all src and remove all target, then result will be removed locales
                    Set<Locale> res = new HashSet<>(srcLocales);
                    res.removeAll(targetLocales);
                    if (res.size() > 0) {
                        removedLocales = new HashSet<>(res);
                    }

                    // add all target and remove all src, then result will be added locales
                    res.clear();
                    res.addAll(targetLocales);
                    res.removeAll(srcLocales);
                    if (res.size() > 0) {
                        addedLocales = new HashSet<>(res);
                    }

                    if (removedLocales != null) {
                        SourceIndex.IndexItem indexItem = new SourceIndex.IndexItem(tag, removedLocales);
                        diffResult.removedIndex.getItems().add(indexItem);
                    }

                    if (addedLocales != null) {
                        SourceIndex.IndexItem indexItem = new SourceIndex.IndexItem(tag, addedLocales);
                        diffResult.addedIndex.getItems().add(indexItem);
                    }
                }
            }
        }

        return diffResult;
    }

    public static class DiffResult {
        /**
         * index added
         */
        private SourceIndex addedIndex;

        /**
         * index removed
         */
        private SourceIndex removedIndex;

        public DiffResult() {
            this.addedIndex = new SourceIndex();
            this.removedIndex = new SourceIndex();
        }

        public boolean hasAdded() {
            return addedIndex.getItems().size() > 0;
        }

        public boolean hasRemoved() {
            return removedIndex.getItems().size() > 0;
        }

        public SourceIndex getAddedIndex() {
            return addedIndex;
        }

        public void setAddedIndex(SourceIndex addedIndex) {
            this.addedIndex = addedIndex;
        }

        public SourceIndex getRemovedIndex() {
            return removedIndex;
        }

        public void setRemovedIndex(SourceIndex removedIndex) {
            this.removedIndex = removedIndex;
        }
    }
}
