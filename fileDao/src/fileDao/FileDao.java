package fileDao;

import com.google.gson.Gson;
import shared.dao.Dao;
import shared.dao.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileDao implements Dao {



    @Override
    public void saveEvent(int eventId, String endpoint, String json) {
        System.out.println("saving event with id " + eventId);

        Gson gson = new Gson();
        EventDTO eventDTO = new EventDTO(eventId, endpoint, json);
        String eventString = gson.toJson(eventDTO);
        try {
            File source = new File("/tmp/event.ticket");
            String old_data = new Scanner(source).useDelimiter("\\Z").next();
            EventDTOArray eventDTOArray = gson.fromJson(old_data, EventDTOArray.class);
            eventDTOArray.eventDTOS.add(eventDTO);

            FileWriter fileWriter = new FileWriter(source, false);
            fileWriter.write(gson.toJson(eventDTOArray));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveState(int eventId, Object state) {
        Gson gson = new Gson();

        // serialize the object
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(state);
            so.flush();
            String serializedState = bo.toString();


            StateDTO stateDTO = new StateDTO(eventId, serializedState);
            String eventString = gson.toJson(stateDTO);

            File source = new File("/tmp/state.ticket");
            FileWriter fileWriter = new FileWriter(source, false);
            fileWriter.write(gson.toJson(eventString));

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @Override
    public Object loadState() {
        Gson gson = new Gson();

        // deserialize the object
        try {
            File source = new File("/tmp/event.ticket");
            String data = new Scanner(source).useDelimiter("\\Z").next();
            StateDTO stateDTO = gson.fromJson(data, StateDTO.class);

            byte b[] = stateDTO.state.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    @Override
    public List<Event> getEventsAfter(int eventId) {
        Gson gson = new Gson();

        try {
            File source = new File("/tmp/event.ticket");
            String old_data = new Scanner(source).useDelimiter("\\Z").next();
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

        }
        return null;
    }
}
