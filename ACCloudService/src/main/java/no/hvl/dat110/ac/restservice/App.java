package no.hvl.dat110.ac.restservice;

import com.google.gson.Gson;

import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App {

	static AccessLog accesslog = null;
	static AccessCode accesscode = null;

	public static void main(String[] args) {

		if (args.length > 0)
			port(Integer.parseInt(args[0]));
		else
			port(8080);

		// objects for data stored in the service
		accesslog = new AccessLog();
		accesscode = new AccessCode();

		after((req, res) -> {
			res.type("application/json");
		});

		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {

			Gson gson = new Gson();

			return gson.toJson("IoT Access Control Device");
		});

		// implements the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description
		get("/accessdevice/log", (req, res) -> accesslog.toJson());

		post("/accessdevice/log", (req, res) -> {
			Gson gson = new Gson();
			AccessMessage message = gson.fromJson(req.body(), AccessMessage.class);
			accesslog.add(message.getMessage());
			return gson.toJson(accesslog.log);
		});

		get("/accessdevice/log/:id", (req, res) -> {
			Gson gson = new Gson();
			accesscode = gson.fromJson(req.body(), AccessCode.class);
			return gson.toJson(accesscode.getAccesscode());
		});

		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			accesscode = gson.fromJson(req.body(), AccessCode.class);
			return gson.toJson(accesscode.getAccesscode());
		});

		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			return gson.toJson(accesscode.getAccesscode());
		});

		delete("/accessdevice/log", (req, res) -> {
			Gson gson = new Gson();
			accesslog.clear();
			return gson.toJson("Log deleted");
		});
	}
}
