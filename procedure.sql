CREATE PROCEDURE UPDATE_CATEGORY_PARENT (
    @categoryId INT,
    @newParentId INT
)
    AS
BEGIN
    SET NOCOUNT ON;
    IF NOT EXISTS (SELECT 1 FROM category WHERE id = @categoryId)
BEGIN
            RAISERROR ('Category ID does not exist', 16, 1);
            RETURN;
END

    IF @newParentId IS NOT NULL AND NOT EXISTS (SELECT 1 FROM category WHERE id = @newParentId)
BEGIN
            RAISERROR ('New parent category ID does not exist', 16, 1);
            RETURN;
END

    IF @newParentId IS NOT NULL AND EXISTS (
        SELECT 1
        FROM category_closure
        WHERE ancestor_id = @categoryId
          AND descendant_id = @newParentId
    )
BEGIN
            RAISERROR ('Cannot move category under its own descendant', 16, 1);
            RETURN;
END

BEGIN TRANSACTION;
BEGIN TRY
DELETE FROM category_closure
WHERE descendant_id IN (
    SELECT descendant_id
    FROM category_closure
    WHERE ancestor_id = @categoryId
)
  AND ancestor_id IN (
    SELECT ancestor_id
    FROM category_closure
    WHERE descendant_id = @categoryId
);

IF @newParentId IS NULL
BEGIN
UPDATE category SET root = 1 WHERE id = @categoryId;
INSERT INTO category_closure (ancestor_id, descendant_id, depth)
VALUES (@categoryId, @categoryId, 0);
INSERT INTO category_closure (ancestor_id, descendant_id, depth)
SELECT @categoryId, descendant_id, depth + 1
FROM category_closure
WHERE ancestor_id = @categoryId
  AND descendant_id != @categoryId;
END
ELSE
BEGIN
UPDATE category SET root = 0 WHERE id = @categoryId;
INSERT INTO category_closure (ancestor_id, descendant_id, depth)
VALUES (@categoryId, @categoryId, 0);
INSERT INTO category_closure (ancestor_id, descendant_id, depth)
SELECT p.ancestor_id, c.descendant_id, p.depth + c.depth + 1
FROM category_closure p
         CROSS JOIN category_closure c
WHERE p.descendant_id = @newParentId
  AND c.ancestor_id = @categoryId;
END

COMMIT TRANSACTION;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        DECLARE @ErrorSeverity INT = ERROR_SEVERITY();
        DECLARE @ErrorState INT = ERROR_STATE();

        RAISERROR (@ErrorMessage, @ErrorSeverity, @ErrorState);
END CATCH
END;
go

CREATE PROCEDURE DELETE_CATEGORY(
    @categoryId INT
)
    AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @parentId INT;

SELECT TOP 1 @parentId = ancestor_id
FROM category_closure
WHERE descendant_id = @categoryId AND ancestor_id != @categoryId
ORDER BY depth;

BEGIN TRANSACTION;
BEGIN TRY

IF(EXISTS(select top 1 id from product where category = @categoryId))
BEGIN
               RAISERROR('Cannot delete it. This category contains some product.',16,1);
END

        IF @parentId IS NULL
BEGIN
UPDATE category SET root = 1 WHERE id IN (
    SELECT descendant_id FROM category_closure WHERE ancestor_id = @categoryId AND depth = 1
);
END
ELSE
BEGIN
INSERT INTO category_closure (ancestor_id, descendant_id, depth)
SELECT p.ancestor_id, c.descendant_id, p.depth + c.depth
FROM category_closure p
         INNER JOIN category_closure c ON c.ancestor_id = @categoryId
WHERE p.descendant_id = @parentId;
END
DELETE FROM category_closure
WHERE descendant_id IN (
    SELECT descendant_id FROM category_closure WHERE ancestor_id = @categoryId
);

DELETE FROM category WHERE id = @categoryId;

COMMIT TRANSACTION;
END TRY
BEGIN CATCH
ROLLBACK TRANSACTION
END CATCH
END;
go

CREATE   PROCEDURE CREATE_CATEGORY(
    @name NVARCHAR(63),
    @slug NVARCHAR(63),
    @description NVARCHAR(255),
    @parentId INT
)
    AS
BEGIN
        DECLARE @isRoot bit;
        SET @isRoot = 0;
        IF(@parentId is null)
begin
                SET @isRoot = 1;
end
BEGIN TRANSACTION
BEGIN TRY
INSERT INTO category(name,slug,description,root) VALUES (@name,@slug,@description,@isRoot);
                DECLARE @id INT
                SET @id = SCOPE_IDENTITY();
INSERT INTO category_closure values (0,@id,@id);
IF(@parentId is not null)
begin
INSERT INTO category_closure(depth, ancestor_id, descendant_id)
SELECT depth + 1, ancestor_id, @id FROM category_closure
WHERE descendant_id = @parentId;
end
end try
begin catch
ROLLBACK TRANSACTION
end catch
COMMIT TRANSACTION
END
go

