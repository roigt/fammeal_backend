# FamMeal

> Reda Himmi, Bernard Traore, Julio Roigt, Guillaume Broutin

## Configuration

#### Ngrok

Pour exposer localhost sur internet, nous passons par Ngrok. Il faut donc commencer par [s'inscrire](https://dashboard.ngrok.com/signup) sur le site.

Affectez ensuite la valeur de votre token à la variable `NGROK_AUTHTOKEN` dans le fichier `.env` (Votre token est disponible dans la [section Authtoken](https://dashboard.ngrok.com/get-started/your-authtoken)).

![Token Ngrok](imgs/ngrok_token.png)

Ngrok fournit un nom de domaine gratuit.

Créez-en un si ce pas déjà fait dans la [section domaine](https://dashboard.ngrok.com/domains), puis affectez le nom du domaine (xxx.ngrok-free.app) à la variable `NGROCK_DOMAIN_NAME` dans le fichier `.env`.

![Domaine Ngrok](imgs/ngrok_domain.png)
