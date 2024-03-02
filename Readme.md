### Guides

The following guides illustrate how to run the project:

1. **Navigate to the FrontEnd directory:**

   ```bash
   cd FrontEnd
   ```

2. **Install packages**

   ```bash
   npm i
   ```

3. **Build the React project:**

   ```bash
   npm run build
   ```

4. **Copy the build files to the Spring Boot static directory:**

   ```bash
   cp -r build/* ../src/main/resources/static
   ```

5. **Navigate back to the project root directory:**

   ```bash
   cd ..
   ```

6. **Run the Spring Boot application:**

   ```bash
   mvn spring-boot:run
   ```
