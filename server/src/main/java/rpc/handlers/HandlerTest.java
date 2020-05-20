package rpc.handlers;

import org.springframework.stereotype.Component;

@Component("HandlerTest")
public class HandlerTest {
    private String name = "sss";
    private String job = "no job";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
