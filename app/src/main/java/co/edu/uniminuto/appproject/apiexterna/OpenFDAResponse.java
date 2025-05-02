package co.edu.uniminuto.appproject.apiexterna;

import java.util.List;


public class OpenFDAResponse {
    private List<Result> results;

    public  List<Result>getResult(){
        return results;
    }

    public class Result{
        private String bran_name;


    }

}
