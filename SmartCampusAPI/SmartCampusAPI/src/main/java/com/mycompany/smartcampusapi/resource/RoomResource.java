package com.mycompany.smartcampusapi.resource;



import com.mycompany.smartcampusapi.exception.RoomNotEmptyException;
import com.mycompany.smartcampusapi.model.Room;
import com.mycompany.smartcampusapi.store.InMemoryStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Collection<Room> getAllRooms() {
        return InMemoryStore.rooms.values();
    }

    @POST
    public Response createRoom(Room room) {
        if (room == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new LinkedHashMap<String, Object>() {{
                        put("error", "InvalidRequest");
                        put("message", "Request body is required");
                        put("status", 400);
                    }})
                    .build();
        }

        if (room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new LinkedHashMap<String, Object>() {{
                        put("error", "InvalidRoom");
                        put("message", "Room id is required");
                        put("status", 400);
                    }})
                    .build();
        }

        if (room.getName() == null || room.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new LinkedHashMap<String, Object>() {{
                        put("error", "InvalidRoom");
                        put("message", "Room name is required");
                        put("status", 400);
                    }})
                    .build();
        }

        if (room.getCapacity() < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new LinkedHashMap<String, Object>() {{
                        put("error", "InvalidRoom");
                        put("message", "Room capacity cannot be negative");
                        put("status", 400);
                    }})
                    .build();
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<String>());
        }

        InMemoryStore.rooms.put(room.getId(), room);

        return Response.created(URI.create("/api/v1/rooms/" + room.getId()))
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        Room room = InMemoryStore.rooms.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room not found: " + roomId);
        }
        return room;
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = InMemoryStore.rooms.get(roomId);

        if (room == null) {
            throw new NotFoundException("Room not found: " + roomId);
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because sensors are still assigned");
        }

        InMemoryStore.rooms.remove(roomId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Room deleted successfully");
        response.put("roomId", roomId);

        return Response.ok(response).build();
    }
}