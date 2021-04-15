package engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Scanner;

import com.google.gson.*;

import entity.Player;
import gameMap.Block;
import gameMap.FactoryBlock;
import gameShop.Shop;

public class GameSaver {
	private final String dot = ".";
	private final String fileName = "saves.txt";
	private Gson gson;

	public GameSaver() {
		InterfaceAdapter sus = new InterfaceAdapter();

		gson = new GsonBuilder().registerTypeAdapter(Block.class, sus).registerTypeAdapter(Game.class, sus).registerTypeAdapter(Shop.class, sus).setPrettyPrinting().create();
	}

	public void save(Game game) {
		String json = gson.toJson(game);

		try (FileWriter writer = new FileWriter(dot + File.separator + fileName)) {
			gson.toJson(game, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isSavingPresent() {
		return new File(dot + File.separator + fileName).exists();
	}
	
	
	public GameImpl load() {
		try {
			File myObj = new File(dot + File.separator + fileName);
			String json = "";
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				json += myReader.nextLine();
			}
			myReader.close();

			return gson.fromJson(json, GameImpl.class);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private class InterfaceAdapter<T> implements JsonDeserializer<T>, JsonSerializer<T> {

		private static final String CLASSNAME = "CLASSNAME";
		private static final String DATA = "DATA";

		public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
			jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
			return jsonObject;
		}

		public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
				throws JsonParseException {

			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
			String className = prim.getAsString();
			Class klass = getObjectClass(className);
			return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
		}

		public Class getObjectClass(String className) {
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				// e.printStackTrace();
				throw new JsonParseException(e.getMessage());
			}
		}

	}

}
