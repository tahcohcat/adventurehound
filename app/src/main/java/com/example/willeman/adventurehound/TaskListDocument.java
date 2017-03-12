package com.example.willeman.adventurehound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by WILLEMAN on 7/20/2016.
 */
    public class TaskListDocument implements Comparable<TaskListDocument> {

    private String docId;
    private String docRev;

    private String activity;
    private String details;
    private List<String> tags;
    private Map<String, String> attributes;

    private boolean hasBeenDone;

    public TaskListDocument(
            String docId,
            String activity,
            String details,
            boolean hasBeenDone,
            List<String> tags,
            Map<String,String> attributes)
    {
        this.docId = docId;
        this.activity = activity;
        this.details = details;
        this.tags = tags;
        this.attributes = attributes;
        this.hasBeenDone = hasBeenDone;
    }

    public String getDocId()
    {
        return this.docId;
    }
    public String getActivity()
    {
        return this.activity;
    }
    public String getDetails()
    {
        return this.details;
    }
    public List<String> getTags() { return this.tags;}
    public Map<String,String> getAttributes() { return this.attributes;}


    //Implement the natural order for this class
    public int compareTo(TaskListDocument doc)
    {
        return this.getActivity().toLowerCase().compareTo(doc.getActivity().toLowerCase());
    }

    //Implement the natural order for this class
    public int compareToRating(TaskListDocument doc)
    {
        return this.getAttribute("rating").compareTo(doc.getAttribute("rating"));
    }


    //TODO: to be retired soon
    public String getCategory()
    {
        for (String tag : tags)
        {
            List<String> list = new ArrayList<String>(Arrays.asList(tag.split(":")));
            if (!list.isEmpty() && list.get(0).equalsIgnoreCase("category"))
            {
                return list.get(1);
            }
        }
        return "default";
    }

    public boolean isInCategorySet(String category)
    {
        for (String tag : tags)
        {
            List<String> list = new ArrayList<String>(Arrays.asList(tag.split(":")));
            if (!list.isEmpty() && list.get(0).equalsIgnoreCase("category"))
            {
                return list.get(1).contains(category.toLowerCase());
            }
        }
        return false;
    }

    public boolean isNew()
    {
        return this.docId.isEmpty();
    }

    public void setActivity(String activity)
    {
        this.activity = activity;
    }
    public void setDetails(String details)
    {
        this.details = details;
    }

    public void setDocumentId(String documentId) {
        this.docId = documentId;
    }

    public String getAttribute(String wantedKey) {
        for (Map.Entry<String,String> entry : attributes.entrySet()) {
            if (entry.getKey().toLowerCase().contains(wantedKey.toLowerCase())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean hasAttribute(String wantedKey) {
        return getAttribute(wantedKey)!=null;
    }


    public void updateAttribute(
            String key,
            String value) {

        attributes.put(key,value);
    }

    public boolean addAttribute(String key, String value) {

        if (isKeyReservedWord(key))
        {
            //this is the way we are currently adding our
            //attributes in default listing.
            //should restore this once list has been read in.
            //return false;
        }

        attributes.put(key,value);
        return true;
    }

    public boolean hasBeenDone() {
        return this.hasBeenDone;
    }

    private boolean isKeyReservedWord(String key) {
        try {
            AttributeTypes isOkay = AttributeTypes.valueOf(key);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public void setHasBeenDone(boolean hasBeenDone) {
        this.hasBeenDone = hasBeenDone;
    }
}