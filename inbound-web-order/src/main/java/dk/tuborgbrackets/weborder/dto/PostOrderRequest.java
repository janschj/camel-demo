package dk.tuborgbrackets.weborder.dto;

public class PostOrderRequest {

    String name;

    public PostOrderRequest() {}

    public PostOrderRequest(String name) {
		this.name = name;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}