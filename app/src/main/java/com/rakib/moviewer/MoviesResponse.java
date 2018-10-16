package com.rakib.moviewer;

import java.util.List;

/**
 * Created by Rakib on 1/5/2018.
 */

public class MoviesResponse {
    private int page;

    public int getPage() { return this.page; }

    public void setPage(int page) { this.page = page; }

    private int total_results;

    public int getTotalResults() { return this.total_results; }

    public void setTotalResults(int total_results) { this.total_results = total_results; }

    private int total_pages;

    public int getTotalPages() { return this.total_pages; }

    public void setTotalPages(int total_pages) { this.total_pages = total_pages; }

    private List<Result> results;

    public List<Result> getResults() { return this.results; }

    public void setResults(List<Result> results) { this.results = results; }
}
