
# KotNews | A Kotlin-powered News App

It is a news tracking application developed with Kotlin. MVVM Clean Architecture has been used, and it includes popular and up-to-date libraries.

The purpose of this repository is to serve as a guide for beginners.

![cover_image](https://user-images.githubusercontent.com/50443794/221132843-afb1e82f-c7e1-4fdb-a695-bcec2d10d26b.png)


## Setup

* Clone the repository
* Create a file named ```api.properties``` in the root folder
* Define two constant variables named ```API_BASE_URL``` and ```API_KEY``` inside it.
* Get an API key from ```https://newsapi.org```

root folders | api.properties 
:-------------------------------------:|:-------------------------------------:
![image](https://user-images.githubusercontent.com/50443794/221001960-c3f468ab-bf9b-4746-be5e-afc53d3803f2.png) | ![image](https://user-images.githubusercontent.com/50443794/221002956-c2f09b7d-2ad9-48b4-8aca-9f0ecc4b6f2e.png)
 
## Project Folder Structure

Project Folder Structure | Description
:-------------------------------------:|:-------------------------------------:
![](https://user-images.githubusercontent.com/50443794/221004505-0f6c3e97-8b9e-4c2d-990d-b78ccd65a1ab.png) | The folder structure of the project is as shown on the left. The Clean Architecture approach has been used. Below are the explanations and details of the folders.

* core
* data
* di
* domain
* presentation


#### ```core``` folder

core Folder | Description
:-------------------------------------:|:-------------------------------------:
![](https://user-images.githubusercontent.com/50443794/221006278-e8619d3e-beaf-4e78-bb8e-561d2ff882d1.png) | This folder generally contains semi-independent classes from the project, project-specific constants, enums, and utilities.


#### ```data``` folder

data Folder | Description
:-------------------------------------:|:-------------------------------------:
![](https://user-images.githubusercontent.com/50443794/221006548-8a27a7fb-57c3-4a4e-985e-4ac7f74d2fbc.png) | This folder contains classes where we get, manage, and distribute the data.


#### ```di``` folder

di Folder | Description
:-------------------------------------:|:-------------------------------------:
![](https://user-images.githubusercontent.com/50443794/221006851-644f0a54-38d3-46ff-a58d-ab2012d48185.png) | This folder is where we manage dependency injection.

#### ```domain``` folder

domain Folder | Description
:-------------------------------------:|:-------------------------------------:
![](https://user-images.githubusercontent.com/50443794/221007784-ddd7bf1c-fb15-407f-bba2-25162d077097.png) | This folder is where we can access data from a single repository and manage our use-cases

#### ```presentation``` folder

presentation Folder | Description
:-------------------------------------:|:-------------------------------------:
![](https://user-images.githubusercontent.com/50443794/221008246-c5117d93-7fa3-40da-a88c-c55070f6d66e.png) | This folder is where we manage our UI components. We manage many components such as Activities, Fragments, ViewModels, and Adapters in this folder.

#### ```App.kt``` file

This class extends the ```Application``` class and is annotated with the ```@HiltAndroidApp``` annotation.

```
@HiltAndroidApp
class App: Application()
```
