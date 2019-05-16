package com.gymkhanachain.app.model.beans;

import com.google.android.gms.maps.model.LatLng;
import com.gymkhanachain.app.commons.ProxyBitmap;

import java.util.List;

public class QuizzPointBean extends PointBean {
    String question;
    List<String> solutions;
    int solution;

    public QuizzPointBean(final Integer id, final String name, final String description,
                          final ProxyBitmap image, final LatLng position, final String question,
                          final List<String> solutions, final int solution) {
        super(id, name, description, image, position);
        setQuestion(question).setSolutions(solutions).setSolution(solution);
    }


    public String getQuestion() {
        return question;
    }

    public QuizzPointBean setQuestion(String question) {
        this.question = question;
        return this;
    }

    public List<String> getSolutions() {
        return solutions;
    }

    public QuizzPointBean setSolutions(List<String> solutions) {
        this.solutions = solutions;
        return this;
    }

    public int getSolution() {
        return solution;
    }

    public QuizzPointBean setSolution(int solution) {
        this.solution = solution;
        return this;
    }
}
