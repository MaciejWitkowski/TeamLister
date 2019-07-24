# TeamLister

## Application overview:
Android application to read teams from photo of teamsheet and write them into .txt files.
###### Supports photos from:
- In-app camera.
- In-app gallery
- Camera gallery.

###### Text formatting options:
- Number location(start/end)
- Number append/prepend
- Case formatting(default/lower/UPPER/Capitalized)
- Replace non ASCII characters
- Remove brackets(none/all/not closed)
- Autofix common text recognition errors

###### File/Folder options:
- Output saved to 1 or 2 files
- File names entered by the user
- Folder name entered by the user

###### Other features:
- Users can report and send non properly analyzed photos.
- Auto cleanup of old files(never/1/2/4 weeks)

## Technologies, libraries, architectural patterns and APIs used:
- Kotlin
- MVVM with Livedata
- CameraX
- Firebase ML text recognition
- Firebase storage
- RxJava2
- RxBinding
- Room
- Glide
- Coroutines
- Junit


Requires Firebase google-services.json to work. Firebase bucket URL is stored in gradle.properties file.