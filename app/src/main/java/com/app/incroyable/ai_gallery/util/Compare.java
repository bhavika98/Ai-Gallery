package com.app.incroyable.ai_gallery.util;

import com.app.incroyable.ai_gallery.model.Folder;
import com.app.incroyable.ai_gallery.model.ThumbImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Compare {

    public static ArrayList<Folder> byName(int angleName, ArrayList<Folder> folderArrayList) {
        if (angleName == 1) {
            Collections.sort(folderArrayList, new Comparator<Folder>() {
                public int compare(Folder o1, Folder o2) {
                    if (o1.getName() == null || o2.getName() == null)
                        return 0;
                    return o2.getName().compareToIgnoreCase(o1.getName());
                }
            });
        } else {
            Collections.sort(folderArrayList, new Comparator<Folder>() {
                public int compare(Folder o1, Folder o2) {
                    if (o1.getName() == null || o2.getName() == null)
                        return 0;
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
        }

        return folderArrayList;
    }

    public static ArrayList<Folder> byDate(int angleDate, ArrayList<Folder> folderArrayList) {
        if (angleDate == 1) {
            Collections.sort(folderArrayList, new Comparator<Folder>() {
                public int compare(Folder o1, Folder o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return o2.getDate().compareToIgnoreCase(o1.getDate());
                }
            });
        } else {
            Collections.sort(folderArrayList, new Comparator<Folder>() {
                public int compare(Folder o1, Folder o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return o1.getDate().compareToIgnoreCase(o2.getDate());
                }
            });
        }

        return folderArrayList;
    }

    public static ArrayList<String> byDateCover(ArrayList<String> stringArrayList) {
        Collections.sort(stringArrayList, new Comparator<String>() {

            @Override
            public int compare(String arg0, String arg1) {
                SimpleDateFormat format = new SimpleDateFormat("MMM yyyy");
                int compareResult = 0;
                try {
                    Date arg0Date = format.parse(arg0);
                    Date arg1Date = format.parse(arg1);
                    compareResult = arg0Date.compareTo(arg1Date);
                } catch (Exception e) {
                    e.printStackTrace();
                    compareResult = arg0.compareTo(arg1);
                }
                return compareResult;
            }
        });

        return stringArrayList;
    }

    public static ArrayList<Folder> byManual(ArrayList<Folder> folderArrayList) {
        Collections.sort(folderArrayList, new Comparator<Folder>() {
            @Override
            public int compare(Folder o1, Folder o2) {
                return Integer.valueOf(o1.getPosition()).compareTo(o2.getPosition());
            }
        });

        return folderArrayList;
    }

    public static ArrayList<ThumbImage> sortImages(int order, ArrayList<ThumbImage> thumbImageArrayList) {
        switch (order) {
            case 00:
                Collections.sort(thumbImageArrayList, new Comparator<ThumbImage>() {
                    public int compare(ThumbImage o1, ThumbImage o2) {
                        if (o1.getName() == null || o2.getName() == null)
                            return 0;
                        return o2.getName().compareToIgnoreCase(o1.getName());
                    }
                });
                break;

            case 01:
                Collections.sort(thumbImageArrayList, new Comparator<ThumbImage>() {
                    public int compare(ThumbImage o1, ThumbImage o2) {
                        if (o1.getName() == null || o2.getName() == null)
                            return 0;
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                break;

            case 10:
                Collections.sort(thumbImageArrayList, new Comparator<ThumbImage>() {
                    public int compare(ThumbImage o1, ThumbImage o2) {
                        if (o1.getDate() == null || o2.getDate() == null)
                            return 0;
                        return o2.getDate().compareToIgnoreCase(o1.getDate());
                    }
                });
                break;

            case 11:
                Collections.sort(thumbImageArrayList, new Comparator<ThumbImage>() {
                    public int compare(ThumbImage o1, ThumbImage o2) {
                        if (o1.getDate() == null || o2.getDate() == null)
                            return 0;
                        return o1.getDate().compareToIgnoreCase(o2.getDate());
                    }
                });
                break;

            case 20:
                Collections.sort(thumbImageArrayList, new Comparator<ThumbImage>() {
                    @Override
                    public int compare(ThumbImage o1, ThumbImage o2) {
                        return Long.valueOf(o2.getSize()).compareTo(o1.getSize());
                    }
                });
                break;

            case 21:
                Collections.sort(thumbImageArrayList, new Comparator<ThumbImage>() {
                    @Override
                    public int compare(ThumbImage o1, ThumbImage o2) {
                        return Long.valueOf(o1.getSize()).compareTo(o2.getSize());
                    }
                });
                break;

            case 30:
                Collections.sort(thumbImageArrayList, new Comparator<ThumbImage>() {
                    @Override
                    public int compare(ThumbImage o1, ThumbImage o2) {
                        return Integer.valueOf(o2.getId()).compareTo(o1.getId());
                    }
                });
                break;

            case 31:
                Collections.sort(thumbImageArrayList, new Comparator<ThumbImage>() {
                    @Override
                    public int compare(ThumbImage o1, ThumbImage o2) {
                        return Integer.valueOf(o1.getId()).compareTo(o2.getId());
                    }
                });
                break;
        }
        return thumbImageArrayList;
    }
}
