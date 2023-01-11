# MagnetometerDataCollection
 
Using the app:
On upon opening app user can read instructions how to put phone in appropriate stage.
For now there are 2 stages (Stage 5 - All-Off and Stage 6 - All-On)
 - For stage 5 user needs to turn off Bluetooth, mobile data and Wi-Fi. And turn on Airplane mode.
 - For stage 6 user needs to turn on Bluetooth, mobile data and Wi-Fi. And turn off Airplane mode.
Example of screen

<img src="https://user-images.githubusercontent.com/93483397/211882008-70146d09-79bc-464c-aea1-a4118f122067.png" width="300" height="600"><img src="https://user-images.githubusercontent.com/93483397/211887426-4648328c-0176-44d0-b833-9c78e9d9059a.png" width="300" height="600">

When ready user should click on button "Ready" to proceed!
If conditions are not fulfilled app will warn user to check conditions again.

<img src="https://user-images.githubusercontent.com/93483397/211883184-f4e0f211-42b8-4878-9c8c-25a59c1b108c.png" width="300" height="425">

In next activity user needs to enter file name (description of data) and folder name. If folder doesn’t exist on Firebase server, then new folder with given name will be created.

<img src="https://user-images.githubusercontent.com/93483397/211883752-701f7d53-70ad-45d6-ad46-6244cefa0110.png" width="300" height="600">

After that data can be collected by clicking on button "Start collecting".
Upon collecting data is done, app will position itself on first activity again. There next stage needs to be set up and process repeated as before.

If last stage to be collected doesn't include internet connection app will inform user when to turn on the internet, so the files can be sent to server. 



