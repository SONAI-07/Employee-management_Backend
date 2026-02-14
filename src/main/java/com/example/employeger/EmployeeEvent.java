package com.example.employeger;


import org.springframework.context.ApplicationEvent;


public class EmployeeEvent extends ApplicationEvent
{

    public enum EventType  // Event-Types
    {
        SIGNUP,
        LEAVE_REQUEST
    }

        private final EventType eventType;
        private final BacklogBase backlogbase;


        public EmployeeEvent(Object source, EventType eventType, BacklogBase backlogbase)
        {
            super(source);
            this.eventType = eventType;
            this.backlogbase=backlogbase;
        }

        public EventType getEventType()
        {
            return eventType;
        }

        public BacklogBase getBacklogBase()
        {
            return backlogbase;
        }


    }







