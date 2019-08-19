package com.metamug.mason.io.mpath;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.taglibs.standard.tag.common.sql.ResultImpl;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author anishhirlekar
 */
public class ResultExtractStrategy extends ExtractStrategy{

    @Override
    public String extract(String path, Object target) {
        ResultImpl ri = (ResultImpl)target;
  
        int rowIndex = Integer.parseInt( getRow(path) );
        //throw exception if given row index is greater than row count
        if( (rowIndex+1) > ri.getRowCount() ) {
            throw new ArrayIndexOutOfBoundsException("Given row index [" + rowIndex + "] is greater"
                    + " than number of records ("+ri.getRowCount()+") in SQL result.");
        }
        //throw exception if given column name not found in result
        String colName = getColumn(path);
        if(!Arrays.asList(ri.getColumnNames()).contains(colName)){
            throw new NoSuchElementException("Column name \""+colName+"\" not found in SQL result.");
        }
        
        SortedMap row = ri.getRows()[rowIndex];
        return row.get(colName).toString();
    }
    
      
    /**
     * Method takes MPath value and returns row index
     *
     * @param path MPath string
     * @return row index
     */
    public String getRow(String path){
        String l = getLocator(path);
        Pattern p = Pattern.compile("^\\[(.*?)\\]");// [1],[2],...
        Matcher m = p.matcher(l);
        //if row notation not given, default to [0]
        String r = "0";
        
        while(m.find()) {
            r = m.group(1);
        }
   
        return r;
    }
    /**
     * Method takes MPath value and returns column name
     *
     * @param path MPath string
     * @return var column name
     */
    public String getColumn(String path){
        String col = getLocator(path).replaceFirst("\\[(.*?)\\]\\.", "");  
        col = col.startsWith(".") ? col.substring(1) : col;
        return col;
    }
}
