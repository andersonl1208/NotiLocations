
# NotiLocations
How many times have you had a task to do outside of the house, but forgot the next time you were near that location? Our app is here to solve that problem. This app introduces a simple way for people to manage errands and tasks that many may forget when out and about in their daily lives. Connecting the app to your phone’s location allows the user to input various tasks or errands that need to be done, such as stopping at the grocery store to pick up a few items, or the pharmacy to pick up a prescription. Simply input or select a specified location on the map and input a goal for the location, such as a shopping list that will be sent to the user when within the specified radius which can then be opened and viewed.

Included safety features set by the user are used to ensure there is no distracted driving. The user simply inputs a specific speed of which notifications will be muted when travelling above that speed. Once the user has slowed below that the notification is pushed if the user has arrived at a specified location or area within the set radius. The user will receive the notification for that location such as the shopping list or a reminder stating whatever the user has set as the goal for that location. Once the user has completed their goal for the location, they can set the notification to completed. The task will then be removed from their active list. If the user did not complete what they set as a task for that location, the user can opt to decline the completion, and the task will remain active and continue to push notifications in the future.

# Description

Upon first opening the app after installation, the user will see a loading screen with the app’s logo in the middle. If the user does not have location services enabled, they will be prompted to give the app permission to access their location. If the user decides to not allow location services, they are still allowed to use the app, but they will be alerted that functionality will be limited. Once the app is done initially loading, the user will then have the option to create a new notification so that they can be alerted when they are near a location they want to do something at.

After selecting the button to create a new notification, the user will be prompted with a form to enter a name for the notification, optional time chunk to allow the notification, optional expiration time/date, optional text to be read aloud when the notification is triggered, and a location from Google Maps to choose to trigger the notification. Alternatively, they could select a type of location such as a grocery store or hairdressers to use to determine when to trigger the notification. Afterwards, the user will select the confirm button and the new notification will be created. From there, the user will be sent to the main screen with a list of all the named notifications the user has created.

To delete a notification, the user will either swipe left on the notification to show a red trash can, which when selected will temporarily delete the notification. After this, a bar will pop up from the bottom prompting the user to undo the delete of the notification. If undo is selected, the notification is put back. If ignored, the notification is permanently deleted from the view and the database.

To edit a notification, the user can click on it and it will pop up a form similar to the new notification form only with the editable notification information already filled in. The user can choose to save the edit or cancel the edit, which will not save the changes made to the notification.

When the user is near a location they have created, a notification for the notification’s description will be sent to the user’s phone. If the phone is not muted and the user has enabled the notification reading setting, the notification will be read aloud. The user can then decide whether to complete the notification, which will cause that particular notification to not be sent in the future. The user can then complete their task at that location.

If the user does a general right swipe on the home screen will bring the user to a Google Maps API screen with all the locations they have selected. If the user has location services allowed, the map auto centers around the user’s location.

If the user wants to adjust settings, they click on the settings gear on the upper right of the home screen and the general settings menu will pop up. The user can then change the notification radius trigger distance, the frequency of notifications, the app’s theme, the max speed to provide notifications at, and a general active time for the app.

The user can also link their notification list with family members or roommates. The user and the person they are linking with select the settings button and then select link notifications. They then both can view a unique code for their device and enter the other person’s unique code to link their notifications. The users can then view each other’s notifications in the notification list.

# Tasks
## 1. Manage location
- Add active location to a task in the list
- Edit a location to change/add a task or to change the location for a specified task

Allowing the user to be able to manage the location where the task is at is extremely important. This is because the location determines when the notification will pop up. Also, the user needs to be able to change the location. If the user sets the wrong location or simply wishes to change to a new location, they should be able to do that. For example, if the user moves to a new place, they are probably going to different stores and will need to change the locations of the reminders.


## 2. Manage tasks
- Add a task to the list
- Edit task’s details
- Remove a task from the list

The user needs to be able to add a task that they wish to have a reminder for, or they won’t be able to get any notifications. The user will also to edit the current tasks that already exist. This is important in case the user wants to do an extra task at the location. Finally, allowing the user to remove a task is important as when you have accomplished a task you don’t want to keep getting re-reminded about a task that you have already completed.


## 3. Trigger a notification
- If the user is traveling at a certain speed, don’t read the notification
- Push notifications to user once within specified radius/range
- Offer several options for pushing notifications (text, voice, push)

The delivery of the notifications is paramount to the app functionality. The user will be informed and updated on current tasks in the surrounding area, while also retaining a safe method of doing so. The user also can change how they receive notifications so that they are able to stay safe while driving and not receive notifications at inopportune times.


## 4. Change the settings
- Change required speed to not read notifications
- Change how often the app checks
- Change the range the notification triggers from

The user can change the settings in the app to allow them to have a more custom experience and better meet their specific needs. For instance, the user may not like what speed we have set by default for not reading notifications. The user can also change preferences such as how often the app updates the user’s location and the radius at which the notifications are triggered.



