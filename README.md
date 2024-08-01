**Le storage des fichiers en utilisant Firebase**
<br/>
<img width="141" alt="Capture d’écran 2024-08-01 à 13 49 41" src="https://github.com/user-attachments/assets/ccf20ee6-13cf-4344-bd89-3fbaae1f205b">
<br/>
**il faut ajouter ces régles dans firbase  :**
<br/>
service firebase.storage {<br/>
  match /b/{bucket}/o {<br/>
    match /{allPaths=**} {<br/>
      allow read, write: if true;<br/>
    }<br/>
  }<br/>
}<br/>
<br/>
