package com.example.myapplication.Home.Animal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "response")
public class AnimalResponse {
    @Element(name = "head", required = false)
    private Head head;

    @ElementList(name = "row", inline = true, required = false)
    private List<AnimalItem> animals;

    public Head getHead() {
        return head;
    }

    public List<AnimalItem> getAnimals() {
        return animals;
    }

    public static class Head {
        @Element(name = "CODE")
        private String code;

        @Element(name = "MESSAGE")
        private String message;

        @Element(name = "RESULT", required = false)
        private String result;

        @Element(name = "list_total_count", required = false)
        private int listTotalCount;

        @Element(name = "api_version", required = false)
        private String apiVersion;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getResult() {
            return result;
        }

        public int getListTotalCount() {
            return listTotalCount;
        }

        public String getApiVersion() {
            return apiVersion;
        }
    }
}
