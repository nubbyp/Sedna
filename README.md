# SubDomainCrawler

SubDomainCrawler is a Java-based tool designed to crawl websites and identify subdomains. 
This project is built using Maven. It is for a job application to Sedna. 

## 🚀 Features

* Lightweight subdomain enumeration
* Output to console
* HTML content parsing with JSoup

## 🛠️ Technologies

* Java 17+
* Maven
* [JSoup](https://jsoup.org/) (for HTML parsing)

## 📦 Project Structure
```
Sedna/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── SubDomainCrawler
├── pom.xml
└── README.md
```

## ⚙️ Usage

1. **Clone the repository**
   ```bash
   git clone https://github.com/nubbyp/Sedna.git
   
   cd Sedna

2. **Build the project using Maven**
   ```bash
    mvn clean package
   
3. **Run the crawler**
   ```bash
   java -jar target/SubDomainCrawler-1.0-SNAPSHOT.jar SubDomainCrawler


## 📄 Output

Results are printed to the console