![image](app/src/main/res/drawable/okunur_mu_logo.png)
# OkunurMu
OkunurMu is a mobile app that connects high school students and graduates with current university students, assisting them in their university decision-making process. Our app enhances the user experience by offering a range of features such as messaging capabilities, scheduling Zoom meetings, and filtered search options to ensure students find the right tutor and have a seamless interaction throughout their academic journey.

-------
The Contributors:
- Artun Berke Gül 

- Ayşe Vildan Çetin

- Emre Uçar

- Kamil Berkay Çetin
-------

**How to execute the program:**

When you open the app you will see our launch page. On this page if you click login button you will be asked to enter your mail and your personal password. Mail address and password must be entered correctly to use the app.
If you do not have an account, create your account with your university email in sign-up page if you are creating a mentor account. (Currently supporting 5 university mails: Bilkent, Koç, İtü, Sabancı, and Hacettepe). You can use other mail services if you are creating a mentee account. After that your account will be created.

After you log in, you will be welcomed by the profile page. On this page you can see and upload your personal photos and make changes in your profile, such as changin your bio or profile picture. 

In search page, you can search mentors by their name, university, and department. And after you see the results, you can send a message, or meeting request to the tutor

In the chat page, you can see your old messages, and send new ones.

In the scheduled meetings page, you can see your meeting details with your tutor.


-------
**Dependencies:**

constraints{
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0"){
        because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0"){
        because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
    }
}

- androidx.appcompat:appcompat:1.6.1

- androidx.fragment:fragment:1.5.7

- com.google.firebase:firebase-bom:32.0.0

- com.google.gms:google-services:4.3.15

- com.google.firebase:firebase-auth

- com.google.firebase:firebase-storage

- com.google.firebase:firebase-firestore

- com.firebaseui:firebase-ui-storage:7.2.0

- com.github.bumptech.glide:glide:4.15.1

- com.github.bumptech.glide:compiler:4.14.2

- com.google.firebase:firebase-analytics

- org.typesense:typesense-java:0.2.0

- com.google.android.material:material:1.9.0

- androidx.navigation:navigation-ui:2.5.3

- androidx.navigation:navigation-fragment:2.5.3

- androidx.constraintlayout:constraintlayout:2.1.4

- androidx.lifecycle:lifecycle-livedata-ktx:2.6.1

- androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1

- junit:junit:4.13.2

- androidx.test.ext:junit:1.1.5

- androidx.test.espresso:espresso-core:3.5.1
