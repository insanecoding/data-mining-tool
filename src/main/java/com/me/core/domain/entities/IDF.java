//package com.me.core.domain.entities;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//import static javax.persistence.GenerationType.IDENTITY;
//
//@Entity
//@Table(name = "IDF")
//public class IDF implements Serializable{
//
//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    @Column(name = "entry_id")
//    private long entry_id;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="exp_num")
//    private Experiment experiment;
//
//    @Column(name = "term", columnDefinition="TEXT")
//    private String term;
//
//    @Column(name = "value", columnDefinition="TEXT")
//    private String value;
//
//    public long getEntry_id() {
//        return entry_id;
//    }
//
//    public void setEntry_id(long entry_id) {
//        this.entry_id = entry_id;
//    }
//
//    public Experiment getExperiment() {
//        return experiment;
//    }
//
//    public void setExperiment(Experiment experiment) {
//        this.experiment = experiment;
//    }
//
//    public String getTerm() {
//        return term;
//    }
//
//    public void setTerm(String term) {
//        this.term = term;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    @Override
//    public String toString() {
//        return "IDF{" +
//                "entry_id=" + entry_id +
//                ", experiment=" + experiment.getExperimentNumber() +
//                ", term='" + term + '\'' +
//                ", value='" + value + '\'' +
//                '}';
//    }
//}
