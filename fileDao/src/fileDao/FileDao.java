package fileDao;

import com.google.gson.Gson;
import shared.dao.Dao;
import shared.dao.Event;

import java.io.*;
import java.util.*;

public class FileDao implements Dao {

    @Override
    public void saveEvent(int eventId, String endpoint, String json) {
        System.out.println("saving event with id " + eventId);

        Gson gson = new Gson();
        EventDTO eventDTO = new EventDTO(eventId, endpoint, json);
        try {
            File source = new File("/tmp/events.ticket");
            Scanner scanner = new Scanner(source).useDelimiter("\\Z");
            String old_data = "";
            if(scanner.hasNext()) {
                 old_data = scanner.next();
            }
            EventDTOArray eventDTOArray = gson.fromJson(old_data, EventDTOArray.class);
            if(eventDTOArray == null) { eventDTOArray = new EventDTOArray(new ArrayList<>()); }
            if(eventDTOArray.eventDTOS == null) { eventDTOArray.eventDTOS = new ArrayList<>(); }
            eventDTOArray.eventDTOS.add(eventDTO);

            FileWriter fileWriter = new FileWriter(source, false);
            fileWriter.write(gson.toJson(eventDTOArray));
            fileWriter.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace(); System.out.println("save event");

        } catch (IOException e) {
            e.printStackTrace(); System.out.println("save event");
        }
    }

    @Override
    public void saveState(int eventId, Object state) {

        // serialize the object
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(state);
            so.flush();
            byte[] serializedState = bo.toByteArray();

            File stateFile = new File("/tmp/state.ticket");
            FileOutputStream fileOutputStream = new FileOutputStream(stateFile);
            fileOutputStream.write(serializedState);
            fileOutputStream.flush();
//            FileWriter fileWriter = new FileWriter(stateFile, false);
//            fileWriter.write(serializedState);
//            fileWriter.flush();

        } catch (Exception e) {
            e.printStackTrace(); System.out.print("saveState");
        }

    }

    @Override
    public Object loadState() {

        // deserialize the object
        try {
            File source = new File("/tmp/state.ticket");
            Scanner scanner = new Scanner(source);

            if(scanner.hasNext()) {
                List<Byte> bytes = new LinkedList<>();
                while(scanner.hasNextByte()) {
                    bytes.add(scanner.nextByte());
                }
                byte b[] = new byte[bytes.size()];
                int i = 0;
                for(Byte B : bytes) {
                    b[i] = B.byteValue();
                    i++;
                }
                ByteArrayInputStream bi = new ByteArrayInputStream(b);
                ObjectInputStream si = new ObjectInputStream(bi);
                return si.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace(); System.out.print("loadState");
        }

        return null;
    }

    @Override
    public List<Event> getEventsAfter(int eventId) {
        Gson gson = new Gson();
        try {
            File source = new File("/tmp/events.ticket");
            Scanner scanner = new Scanner(source).useDelimiter("\\Z");
            String old_data = "";
            if(scanner.hasNext()) {
                old_data = scanner.next();
            }
            EventDTOArray eventDTOArray = gson.fromJson(old_data, EventDTOArray.class);
            List<EventDTO> eventDTOS = eventDTOArray.eventDTOS;
            Collections.sort(eventDTOS, EventDTO::idCompare);

            int index = 0;
            for(int i = 0; i < eventDTOS.size(); i++) {
                if(eventDTOS.get(i).id == eventId) {
                    index = i;
                    break;
                }
            }

            List<Event> events = new ArrayList<>();
            for(int i = index; i < eventDTOS.size(); i++) {
                events.add(new Event(eventDTOS.get(i).endpoint, eventDTOS.get(i).json));
            }
            return events;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
