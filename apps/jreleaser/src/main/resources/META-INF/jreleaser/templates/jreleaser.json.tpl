{
  "project": {
    "name": "app",
    "groupId": "com.acme",
    "version": "0.0.0-SNAPSHOT",
    "description": "Awesome App",
    "longDescription": "Awesome App",
    "website": "https://acme.com/app",
    "authors": [
      "Joe Cool"
    ],
    "license": "Apache-2",
    "javaVersion': "8"
  },

  "release": {
    "github": {
      "owner": "joecool"
    }
  },

  "distributions": {
    "app": {
      "artifacts": [
        {
          "path": "path/to/{{distributionName}}-{{projectVersion}}.zip"
        }
      ]
    }
  }
}