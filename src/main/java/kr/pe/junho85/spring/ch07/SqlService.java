package kr.pe.junho85.spring.ch07;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
