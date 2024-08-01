**Le storage des fichiers en utilisant Firebase**
<br/>
<img width="213" alt="Capture d’écran 2024-08-01 à 12 48 18" src="https://github.com/user-attachments/assets/d366dcbb-352b-427f-9b2e-77d7ab436630">
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
